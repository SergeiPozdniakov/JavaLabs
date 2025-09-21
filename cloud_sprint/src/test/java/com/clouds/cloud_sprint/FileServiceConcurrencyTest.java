package com.clouds.cloud_sprint;

import com.clouds.cloud_sprint.model.File;
import com.clouds.cloud_sprint.model.FileUploadProgressListener;
import com.clouds.cloud_sprint.model.Users;
import com.clouds.cloud_sprint.services.FileService;
import io.qameta.allure.*;
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

@Epic("Файловые операции")
@Feature("Конкурентная обработка файлов")
@Story("Тестирование конкурентной загрузки и удаления файлов")
@Owner("Команда разработки CloudSprint")
@Severity(SeverityLevel.CRITICAL)
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
    @Description("Тест проверяет корректную работу системы при одновременной загрузке нескольких файлов. " +
            "Используется техника тест-дизайна 'Конкурентное тестирование' для проверки работы системы " +
            "в условиях параллельного доступа. Тест важен для обеспечения стабильности системы при " +
            "многопользовательском режиме работы и предотвращения race conditions.")
    @AllureId("FILE-CONC-001")
    @Step("Проверка конкурентной загрузки файлов")
    void testConcurrentFileUploads() throws Exception {
        Allure.step("Подготовка данных: настройка мока для загрузки файла");
        // Конкурентная загрузка нескольких файлов
        when(multipartFile.isEmpty()).thenReturn(false);
        when(multipartFile.getOriginalFilename()).thenReturn("test.txt");
        when(multipartFile.getContentType()).thenReturn("text/plain");
        when(multipartFile.getSize()).thenReturn(1024L);
        when(multipartFile.getInputStream()).thenReturn(new ByteArrayInputStream("test content".getBytes())); // ✅ Исправлено!
        when(fileRepository.save(any(File.class))).thenReturn(new File());

        Allure.step("Запуск конкурентной загрузки двух файлов");
        CompletableFuture<File> future1 = fileService.addFile(multipartFile, testUser);
        CompletableFuture<File> future2 = fileService.addFile(multipartFile, testUser);

        CompletableFuture.allOf(future1, future2).join();

        Allure.step("Проверка завершения задач");
        assertTrue(future1.isDone());
        assertTrue(future2.isDone());
        verify(progressListener, times(2)).reset();
    }

    @Test
    @Description("Тест проверяет корректную работу системы при одновременном удалении нескольких файлов. " +
            "Используется техника тест-дизайна 'Конкурентное тестирование' для проверки работы системы " +
            "в условиях параллельного доступа. Тест важен для обеспечения целостности данных при " +
            "многопользовательском режиме работы и предотвращения конфликтов при удалении файлов.")
    @AllureId("FILE-CONC-002")
    @Step("Проверка конкурентного удаления файлов")
    void testConcurrentFileDeletions() throws Exception {
        Allure.step("Подготовка данных: создание двух файлов для удаления");
        //  Конкурентное удаление файлов
        File file1 = new File();
        file1.setId(1L);
        file1.setFilePath(System.getProperty("java.io.tmpdir") + "/file1.txt");

        File file2 = new File();
        file2.setId(2L);
        file2.setFilePath(System.getProperty("java.io.tmpdir") + "/file2.txt");

        when(fileRepository.findById(1L)).thenReturn(java.util.Optional.of(file1));
        when(fileRepository.findById(2L)).thenReturn(java.util.Optional.of(file2));

        Allure.step("Запуск конкурентного удаления файлов");
        CompletableFuture<Void> future1 = fileService.deleteFile(1L);
        CompletableFuture<Void> future2 = fileService.deleteFile(2L);

        CompletableFuture.allOf(future1, future2).join();

        Allure.step("Проверка завершения задач");
        assertTrue(future1.isDone());
        assertTrue(future2.isDone());
        verify(fileRepository, times(2)).deleteById(anyLong());
    }
}
