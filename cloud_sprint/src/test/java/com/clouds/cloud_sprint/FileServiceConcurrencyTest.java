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
}
