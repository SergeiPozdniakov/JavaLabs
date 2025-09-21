package com.clouds.cloud_sprint;

import com.clouds.cloud_sprint.controller.HomeController;
import com.clouds.cloud_sprint.model.File;
import com.clouds.cloud_sprint.model.Users;
import com.clouds.cloud_sprint.services.FileService;
import io.qameta.allure.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicReference;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Epic("Управление файлами в облачном хранилище")
@Feature("Основные операции с файлами")
@Story("Интеграционное тестирование контроллера HomeController")
@Owner("Команда разработки CloudSprint")
@Severity(SeverityLevel.CRITICAL)
class IntTest {

    private static final Long TEST_USER_ID = 1L;
    private static final String TEST_FILE_NAME = "testFile.txt";
    private static final String TEST_USER_FOLDER = "testFolder";
    private static final String TEST_FILE_CONTENT = "test content";
    private static final String BASE_URL = "/home";

    private MockMvc mockMvc;

    @Mock
    private FileService fileService;

    @InjectMocks
    private HomeController homeController;

    private Users testUser;

    @BeforeEach
    @DisplayName("Инициализация виртуального тестового пользователя")
    @Step("Подготовка тестового окружения")
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(homeController).build();
        testUser = createTestUser();

        Allure.addAttachment("Тестовый пользователь",
                "ID: " + testUser.getId() + ", Папка: " + testUser.getBaseFolderPath());
    }

    private Users createTestUser() {
        Users user = new Users();
        user.setBaseFolderPath(TEST_USER_FOLDER);
        user.setId(TEST_USER_ID);
        return user;
    }

    @Test
    @DisplayName("Проверка добавления файла в облачное хранилище")
    @Description("Тест проверяет корректность загрузки файла через HTTP запрос. " +
            "Цель: убедиться, что файл успешно передается в систему и возвращается корректный HTTP статус. " +
            "Используется техника тест-дизайна 'Классы эквивалентности' для проверки работы системы " +
            "с корректными данными. Тест важен для обеспечения корректной загрузки файлов.")
    @AllureId("INT-UPLOAD-001")
    @Step("Создание тестового файла")
    void addFile() throws Exception {
        AtomicReference<MockMultipartFile> testFileRef = new AtomicReference<>();

        Allure.step("Создание тестового файла", () -> {
            MockMultipartFile testFile = new MockMultipartFile("file", TEST_FILE_NAME, "text/plain", TEST_FILE_CONTENT.getBytes());
            Allure.addAttachment("Файл для загрузки",
                    "Имя: " + TEST_FILE_NAME + ", Размер: " + testFile.getSize() + " байт");
            testFileRef.set(testFile);
        });

        Allure.step("Выполнение запроса на загрузку файла", () -> {
            mockMvc.perform(multipart(BASE_URL + "/upload")
                            .file(testFileRef.get())
                            .principal(() -> testUser.getUsername()))
                    .andExpect(status().is3xxRedirection());
        });

        Allure.step("Проверка вызова сервиса", () -> {
            verify(fileService, times(1)).addFile(any(), any());
        });
    }

    @Test
    @DisplayName("Проверка успешного скачивания файла")
    @Description("Тест проверяет механизм скачивания файла из системы. " +
            "Цель: убедиться, что файл корректно находится, возвращаются правильные HTTP заголовки " +
            "и содержимое доступно для скачивания. " +
            "Используется техника тест-дизайна 'Классы эквивалентности' для проверки работы системы " +
            "с корректными данными. Тест важен для обеспечения корректного скачивания файлов.")
    @AllureId("INT-DOWNLOAD-001")
    @Step("Создание тестового файла и настройка файловой системы")
    void downloadFile_Success() throws Exception {
        // 1. Подготовка мока FileService
        Allure.step("Подготовка мока FileService", () -> {
            File mockFile = new File();
            mockFile.setId(TEST_USER_ID);
            mockFile.setFileName(TEST_FILE_NAME);
            mockFile.setFilePath(TEST_USER_FOLDER + "/" + TEST_FILE_NAME);
            // Установите только те поля, которые есть в классе File

            when(fileService.getFileById(TEST_USER_ID)).thenReturn(mockFile);
            Allure.addAttachment("Настройка мока",
                    "FileService.getFileById(" + TEST_USER_ID + ") вернет файл: " + mockFile.getFileName());
        });

        // 2. Создание реального тестового файла
        Allure.step("Создание тестового файла на диске", () -> {
            Path testDir = Paths.get(TEST_USER_FOLDER);
            Files.createDirectories(testDir);

            Path testFile = testDir.resolve(TEST_FILE_NAME);
            Files.write(testFile, TEST_FILE_CONTENT.getBytes());

            Allure.addAttachment("Созданный файл",
                    "Путь: " + testFile + ", Размер: " + Files.size(testFile) + " байт");
        });

        // 3. Выполнение запроса
        Allure.step("Выполнение HTTP запроса на скачивание файла", () -> {
            mockMvc.perform(get(BASE_URL + "/download/{id}", TEST_USER_ID)
                            .principal(() -> testUser.getUsername()))
                    .andExpect(status().isOk())
                    .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + TEST_FILE_NAME + "\""))
                    .andExpect(header().string(HttpHeaders.CONTENT_TYPE, "application/octet-stream"));
        });

        // 4. Проверки
        Allure.step("Проверка вызова сервиса FileService", () -> {
            verify(fileService, times(1)).getFileById(TEST_USER_ID);
            Allure.addAttachment("Результат верификации",
                    "FileService.getFileById() был вызван 1 раз с ID: " + TEST_USER_ID);
        });

        // 5. Очистка
        Allure.step("Очистка тестовых данных", () -> {
            boolean fileDeleted = Files.deleteIfExists(Paths.get(TEST_USER_FOLDER, TEST_FILE_NAME));
            boolean dirDeleted = Files.deleteIfExists(Paths.get(TEST_USER_FOLDER));

            Allure.addAttachment("Очистка файловой системы",
                    "Файл " + (fileDeleted ? "удален" : "не найден") +
                            ", Папка " + (dirDeleted ? "удалена" : "не пустая или не найдена"));
        });
    }

    @Test
    @DisplayName("Проверка успешного удаления файла")
    @Description("Тест проверяет механизм удаления файла из системы. " +
            "Цель: убедиться, что запрос на удаление обрабатывается корректно, " +
            "возвращается правильный HTTP статус и вызывается соответствующий сервис.")
    @AllureId("FILE-DELETE-001")
    void deleteFile_Success() throws Exception {
        Allure.step("Выполнение запроса на удаление файла", () -> {
            mockMvc.perform(post(BASE_URL + "/delete/{id}", TEST_USER_ID)
                            .principal(() -> testUser.getUsername()))
                    .andExpect(status().is3xxRedirection());
        });

        Allure.step("Проверка вызова сервиса удаления", () -> {
            verify(fileService, times(1)).deleteFile(TEST_USER_ID);
        });
    }
}