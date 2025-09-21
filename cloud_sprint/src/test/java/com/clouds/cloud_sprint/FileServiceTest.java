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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Epic("Файловые операции")
@Feature("Сервис работы с файлами")
@Story("Тестирование сервиса FileService")
@Owner("Команда разработки CloudSprint")
@Severity(SeverityLevel.CRITICAL)
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
    @Description("Тест проверяет попытку загрузки пустого файла. " +
            "Используется техника тест-дизайна 'Граничные значения' для проверки работы системы " +
            "с пустыми файлами. Тест важен для обеспечения корректной обработки ошибок при " +
            "попытке загрузить пустой файл.")
    @AllureId("FILE-SERVICE-001")
    @Step("Проверка попытки загрузки пустого файла")
    void testAddFile_EmptyFile() {
        Allure.step("Подготовка данных: настройка мока для пустого файла");

        // Попытка загрузки пустого файла
        when(multipartFile.isEmpty()).thenReturn(true);

        Allure.step("Попытка загрузки пустого файла");
        CompletableFuture<File> result = fileService.addFile(multipartFile, testUser);

        Allure.step("Проверка исключения");
        assertTrue(result.isCompletedExceptionally());
        }

    @Test
    @Description("Тест проверяет попытку загрузки null файла. " +
            "Используется техника тест-дизайна 'Граничные значения' для проверки работы системы " +
            "с null-значениями. Тест важен для обеспечения корректной обработки ошибок при " +
            "попытке загрузить null-файл.")
    @AllureId("FILE-SERVICE-002")
    @Step("Проверка попытки загрузки null файла")
        void testAddFile_NullFile () {
        // Попытка загрузки null файла
        Allure.step("Попытка загрузки null файла");
        CompletableFuture<File> result = fileService.addFile(null, testUser);

        Allure.step("Проверка исключения");
        assertTrue(result.isCompletedExceptionally());
        }

    @Test
    @Description("Тест проверяет успешную загрузку файла. " +
            "Используется техника тест-дизайна 'Классы эквивалентности' для проверки работы системы " +
            "с корректными данными. Тест важен для обеспечения корректной загрузки файлов.")
    @AllureId("FILE-SERVICE-003")
    @Step("Проверка успешной загрузки файла")
    void testAddFile_Success () throws Exception {
        Allure.step("Подготовка данных: настройка мока для загрузки файла");
        // Успешная загрузка файла
        when(multipartFile.isEmpty()).thenReturn(false);
        when(multipartFile.getOriginalFilename()).thenReturn("test.txt");
        when(multipartFile.getContentType()).thenReturn("text/plain");
        when(multipartFile.getSize()).thenReturn(1024L);
        when(multipartFile.getInputStream()).thenReturn(new ByteArrayInputStream("test content".getBytes()));
        when(fileRepository.save(any(File.class))).thenReturn(testFile);

        Allure.step("Запуск загрузки файла");
        CompletableFuture<File> result = fileService.addFile(multipartFile, testUser);

        Allure.step("Проверка результатов загрузки");
        assertNotNull(result);
        assertFalse(result.isCompletedExceptionally());
        File savedFile = result.get();
        assertNotNull(savedFile);
        verify(progressListener).reset();
        verify(fileRepository).save(any(File.class));
    }

    @Test
    @Description("Тест проверяет получение файлов пользователя. " +
            "Используется техника тест-дизайна 'Классы эквивалентности' для проверки работы системы " +
            "с корректными данными. Тест важен для обеспечения корректного получения списка файлов " +
            "пользователя.")
    @AllureId("FILE-SERVICE-004")
    @Step("Проверка получения файлов пользователя")
        void testGetFilesByUser () {
        Allure.step("Подготовка данных: создание списка файлов");
        // Получение файлов пользователя
        List<File> files = Arrays.asList(testFile);
        when(fileRepository.findByUser(testUser)).thenReturn(files);

        Allure.step("Получение файлов пользователя");
        List<File> result = fileService.getFilesByUser(testUser);

        Allure.step("Проверка результатов");
        assertEquals(1, result.size());
        verify(fileRepository).findByUser(testUser);
        }

    @Test
    @Description("Тест проверяет получение файла по ID (найден). " +
            "Используется техника тест-дизайна 'Классы эквивалентности' для проверки работы системы " +
            "с корректными данными. Тест важен для обеспечения корректного получения файла по ID.")
    @AllureId("FILE-SERVICE-005")
    @Step("Проверка получения файла по ID (найден)")
        void testGetFileById_Found () {
        Allure.step("Подготовка данных: настройка мока для существующего файла");
        // Получение файла по ID (найден)
        when(fileRepository.findById(1L)).thenReturn(Optional.of(testFile));

        Allure.step("Получение файла по ID");
        File result = fileService.getFileById(1L);

        Allure.step("Проверка результатов");
        assertNotNull(result);
        assertEquals(1L, result.getId());
        }

    @Test
    @Description("Тест проверяет получение файла по ID (не найден). " +
            "Используется техника тест-дизайна 'Граничные значения' для проверки работы системы " +
            "с несуществующими файлами. Тест важен для обеспечения корректной обработки ошибок при " +
            "попытке получить несуществующий файл.")
    @AllureId("FILE-SERVICE-006")
    @Step("Проверка получения файла по ID (не найден)")
        void testGetFileById_NotFound () {
        Allure.step("Подготовка данных: настройка мока для несуществующего файла");
        // Получение файла по ID (не найден)
        when(fileRepository.findById(1L)).thenReturn(Optional.empty());

        Allure.step("Попытка получения несуществующего файла");
        assertThrows(RuntimeException.class, () -> {
                fileService.getFileById(1L);
        });
        }

    @Test
    @Description("Тест проверяет успешное удаление файла. " +
            "Используется техника тест-дизайна 'Классы эквивалентности' для проверки работы системы " +
            "с корректными данными. Тест важен для обеспечения корректного удаления файлов.")
    @AllureId("FILE-SERVICE-007")
    @Step("Проверка успешного удаления файла")
        void testDeleteFile_Success () throws Exception {
        Allure.step("Подготовка данных: настройка мока для существующего файла");
        // Успешное удаление файла
        when(fileRepository.findById(1L)).thenReturn(Optional.of(testFile));
        testFile.setFilePath("/test/path/test.txt");

        Allure.step("Удаление файла");
        CompletableFuture<Void> result = fileService.deleteFile(1L);

        Allure.step("Проверка результатов удаления");
        assertNotNull(result);
        verify(fileRepository).deleteById(1L);
        }

    @Test
    @Description("Тест проверяет удаление несуществующего файла. " +
            "Используется техника тест-дизайна 'Граничные значения' для проверки работы системы " +
            "с несуществующими файлами. Тест важен для обеспечения корректной обработки ошибок при " +
            "попытке удалить несуществующий файл.")
    @AllureId("FILE-SERVICE-008")
    @Step("Проверка удаления несуществующего файла")
        void testDeleteFile_NotFound () {
        Allure.step("Подготовка данных: настройка мока для несуществующего файла");
        // Удаление несуществующего файла
        when(fileRepository.findById(1L)).thenReturn(Optional.empty());

        Allure.step("Попытка удаления несуществующего файла");
        assertThrows(RuntimeException.class, () -> {
            fileService.deleteFile(1L);
        });
        }
    }


