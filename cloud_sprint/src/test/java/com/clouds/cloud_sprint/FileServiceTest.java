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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FileServiceTest {

    @Mock
    private FileRepository fileRepository;

    @Mock
    private FileUploadProgressListener progressListener;

    @Mock
    private MultipartFile multipartFile;

    @InjectMocks
    private FileService fileService;

    private Users testUser;
    private File testFile;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testUser = new Users();
        testUser.setUsername("testuser");
        testUser.setBaseFolderPath("/test/path");

        testFile = new File();
        testFile.setId(1L);
        testFile.setFileName("test.txt");
    }

    @Test
    void testAddFile_EmptyFile() {

            // Test 39: Попытка загрузки пустого файла
            when(multipartFile.isEmpty()).thenReturn(true);

            CompletableFuture<File> result = fileService.addFile(multipartFile, testUser);

            assertTrue(result.isCompletedExceptionally());
        }

        @Test
        void testAddFile_NullFile () {
            // Test 40: Попытка загрузки null файла
            CompletableFuture<File> result = fileService.addFile(null, testUser);

            assertTrue(result.isCompletedExceptionally());
        }

    @Test
    void testAddFile_Success () throws Exception {
        // Успешная загрузка файла
        when(multipartFile.isEmpty()).thenReturn(false);
        when(multipartFile.getOriginalFilename()).thenReturn("test.txt");
        when(multipartFile.getContentType()).thenReturn("text/plain");
        when(multipartFile.getSize()).thenReturn(1024L);
        when(multipartFile.getInputStream()).thenReturn(new ByteArrayInputStream("test content".getBytes()));
        when(fileRepository.save(any(File.class))).thenReturn(testFile);

        CompletableFuture<File> result = fileService.addFile(multipartFile, testUser);

        assertNotNull(result);
        assertFalse(result.isCompletedExceptionally());
        File savedFile = result.get();
        assertNotNull(savedFile);
        verify(progressListener).reset();
        verify(fileRepository).save(any(File.class));
    }

        @Test
        void testGetFilesByUser () {
            // Получение файлов пользователя
            List<File> files = Arrays.asList(testFile);
            when(fileRepository.findByUser(testUser)).thenReturn(files);

            List<File> result = fileService.getFilesByUser(testUser);

            assertEquals(1, result.size());
            verify(fileRepository).findByUser(testUser);
        }

        @Test
        void testGetFileById_Found () {
            // Получение файла по ID (найден)
            when(fileRepository.findById(1L)).thenReturn(Optional.of(testFile));

            File result = fileService.getFileById(1L);

            assertNotNull(result);
            assertEquals(1L, result.getId());
        }

        @Test
        void testGetFileById_NotFound () {
            // Получение файла по ID (не найден)
            when(fileRepository.findById(1L)).thenReturn(Optional.empty());

            assertThrows(RuntimeException.class, () -> {
                fileService.getFileById(1L);
            });
        }

        @Test
        void testDeleteFile_Success () throws Exception {
            // Успешное удаление файла
            when(fileRepository.findById(1L)).thenReturn(Optional.of(testFile));
            testFile.setFilePath("/test/path/test.txt");

            CompletableFuture<Void> result = fileService.deleteFile(1L);

            assertNotNull(result);
            verify(fileRepository).deleteById(1L);
        }

        @Test
        void testDeleteFile_NotFound () {
            // Удаление несуществующего файла
            when(fileRepository.findById(1L)).thenReturn(Optional.empty());

            assertThrows(RuntimeException.class, () -> {
                fileService.deleteFile(1L);
            });
        }
    }


