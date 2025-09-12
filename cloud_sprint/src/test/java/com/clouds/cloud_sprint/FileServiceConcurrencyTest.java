package com.clouds.cloud_sprint;

import com.clouds.cloud_sprint.model.File;
import com.clouds.cloud_sprint.model.FileUploadProgressListener;
import com.clouds.cloud_sprint.model.Users;
import com.clouds.cloud_sprint.services.FileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FileServiceConcurrencyTest {

    @Mock
    private FileRepository fileRepository;

    @Mock
    private FileUploadProgressListener progressListener;

    @Mock
    private MultipartFile multipartFile;

    @InjectMocks
    private FileService fileService;

    private Users testUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testUser = new Users();
        testUser.setUsername("testuser");
        testUser.setBaseFolderPath(System.getProperty("java.io.tmpdir"));
    }

    @Test
    void testConcurrentFileUploads() throws Exception {
        // Test 70: Конкурентная загрузка нескольких файлов
        when(multipartFile.isEmpty()).thenReturn(false);
        when(multipartFile.getOriginalFilename()).thenReturn("test.txt");
        when(multipartFile.getContentType()).thenReturn("text/plain");
        when(multipartFile.getSize()).thenReturn(1024L);
        when(multipartFile.getInputStream()).thenReturn(new ByteArrayInputStream("test content".getBytes())); // ✅ Исправлено!
        when(fileRepository.save(any(File.class))).thenReturn(new File());

        CompletableFuture<File> future1 = fileService.addFile(multipartFile, testUser);
        CompletableFuture<File> future2 = fileService.addFile(multipartFile, testUser);

        CompletableFuture.allOf(future1, future2).join();

        assertTrue(future1.isDone());
        assertTrue(future2.isDone());
        verify(progressListener, times(2)).reset();
    }

    @Test
    void testConcurrentFileDeletions() throws Exception {
        // Test 71: Конкурентное удаление файлов
        File file1 = new File();
        file1.setId(1L);
        file1.setFilePath(System.getProperty("java.io.tmpdir") + "/file1.txt");

        File file2 = new File();
        file2.setId(2L);
        file2.setFilePath(System.getProperty("java.io.tmpdir") + "/file2.txt");

        when(fileRepository.findById(1L)).thenReturn(java.util.Optional.of(file1));
        when(fileRepository.findById(2L)).thenReturn(java.util.Optional.of(file2));

        CompletableFuture<Void> future1 = fileService.deleteFile(1L);
        CompletableFuture<Void> future2 = fileService.deleteFile(2L);

        CompletableFuture.allOf(future1, future2).join();

        assertTrue(future1.isDone());
        assertTrue(future2.isDone());
        verify(fileRepository, times(2)).deleteById(anyLong());
    }
}
