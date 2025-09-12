package com.clouds.cloud_sprint;

import com.clouds.cloud_sprint.FileRepository;
import com.clouds.cloud_sprint.model.File;
import com.clouds.cloud_sprint.model.FileUploadProgressListener;
import com.clouds.cloud_sprint.model.Users;
import com.clouds.cloud_sprint.services.FileService;

// JUnit 5
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

// Mockito
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

// Spring для MockMultipartFile
import org.springframework.mock.web.MockMultipartFile;

// Java
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

// AssertJ для assertions
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

// Mockito для верификации
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FileServiceEdgeCasesTest {
    @Mock
    FileRepository fileRepository;
    @Mock
    FileUploadProgressListener progressListener;
    @InjectMocks
    FileService fileService;

    private Users user;

    @BeforeEach
    void setUp() {
        user = new Users();
        user.setId(1L);
        user.setUsername("john");
        user.setBaseFolderPath("/home/john");
    }

    @Test
    void addFile_HandlesVeryLargeFile() throws IOException {
        byte[] largeContent = new byte[1024 * 1024]; // 1MB
        MockMultipartFile largeFile = new MockMultipartFile("file", "large.bin", "application/octet-stream", largeContent);
        when(fileRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        CompletableFuture<File> future = fileService.addFile(largeFile, user);

        assertThat(future).isCompleted();
        verify(progressListener, atLeastOnce()).update(anyLong(), eq(1048576L));
    }

    @Test void addFile_HandlesSpecialCharactersInFileName() throws IOException {
        MockMultipartFile file = new MockMultipartFile("file", "файл с пробелами и !@#.txt", "text/plain", "content".getBytes());
        when(fileRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        CompletableFuture<File> future = fileService.addFile(file, user);

        File saved = future.join();
        assertThat(saved.getFileName()).isEqualTo("файл с пробелами и !@#.txt");
    }

    @Test void deleteFile_HandlesNullId() {
        assertThatThrownBy(() -> fileService.deleteFile(null))
                .isInstanceOf(RuntimeException.class);
    }

    @Test void getFileById_HandlesNullId() {
        assertThatThrownBy(() -> fileService.getFileById(null))
                .isInstanceOf(RuntimeException.class);
    }

}
