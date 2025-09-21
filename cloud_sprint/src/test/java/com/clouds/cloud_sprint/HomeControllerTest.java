package com.clouds.cloud_sprint;

import com.clouds.cloud_sprint.model.Users;
import com.clouds.cloud_sprint.services.FileService;
import io.qameta.allure.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.clouds.cloud_sprint.security.SecurityConfig;
import com.clouds.cloud_sprint.model.File;
import com.clouds.cloud_sprint.controller.HomeController;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockMultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Epic("Пользовательский интерфейс")
@Feature("Главная страница и управление файлами")
@Story("Тестирование контроллера HomeController")
@Owner("Команда разработки CloudSprint")
@Severity(SeverityLevel.CRITICAL)
@WebMvcTest(HomeController.class)
@Import(SecurityConfig.class)
class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FileService fileService;

    private Users testUser;

    @BeforeEach
    void setUp() {
        testUser = new Users();
        testUser.setUsername("testuser");

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(
                new UsernamePasswordAuthenticationToken(testUser, null, testUser.getAuthorities())
        );
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    @Description("Тест проверяет отображение главной страницы. " +
            "Используется техника тест-дизайна 'Классы эквивалентности' для проверки работы системы " +
            "с корректными данными. Тест важен для обеспечения корректного отображения главной страницы.")
    @AllureId("UI-HOME-001")
    @Step("Проверка отображения главной страницы")
    void homePage() throws Exception {
        Allure.step("Подготовка данных: настройка мока для пустого списка файлов");
        // *условия выполнения*
        when(fileService.getFilesByUser(any())).thenReturn(Collections.emptyList());

        Allure.step("Выполнение запроса на получение главной страницы");
        // *ожидаемые результаты*
        // GET запрос
        mockMvc.perform(get("/home"))
                .andExpect(status().isOk())     //200
                .andExpect(view().name("home"))
                .andExpect(model().attributeExists("files")); //появление атрибута
    }

    @Test
    @Description("Тест проверяет загрузку валидного файла. " +
            "Используется техника тест-дизайна 'Классы эквивалентности' для проверки работы системы " +
            "с корректными данными. Тест важен для обеспечения корректной загрузки файлов.")
    @AllureId("UI-UPLOAD-001")
    @Step("Проверка загрузки валидного файла")
    void uploadValidFile() throws Exception {
        Allure.step("Подготовка данных: создание тестового файла");
        MockMultipartFile file = new MockMultipartFile(
                "file", "test.txt", "text/plain", "content".getBytes()
        );

        // передаваемый сервису файл
        File savedFile = new File();
        savedFile.setId(1L);
        savedFile.setFileName("test.txt");

        Allure.step("Настройка мока для успешной загрузки файла");
        // *условия выполнения*
        when(fileService.addFile(any(), any()))
                .thenReturn(CompletableFuture.completedFuture(savedFile));

        Allure.step("Выполнение запроса на загрузку файла");
        // *ожидаемые результаты*
        // POST-запрос (для загрузки файлов)
        mockMvc.perform(multipart("/home/upload")
                        .file(file)
                        .with(csrf())) //токен безопасности, иначе 403
                .andExpect(status().is3xxRedirection()) //перенаправление на /home
                .andExpect(redirectedUrl("/home"));
    }

    @Test
    @Description("Тест проверяет загрузку валидного файла. " +
            "Используется техника тест-дизайна 'Классы эквивалентности' для проверки работы системы " +
            "с корректными данными. Тест важен для обеспечения корректной загрузки файлов.")
    @AllureId("UI-UPLOAD-001")
    @Step("Проверка загрузки валидного файла")
    void uploadEmptyFile() throws Exception {
        Allure.step("Подготовка данных: создание пустого файла");
        MockMultipartFile emptyFile = new MockMultipartFile(
                "file", "empty.txt", "text/plain", new byte[0]
        );

        Allure.step("Настройка мока для обработки пустого файла");
        // *условия выполнения*
        when(fileService.addFile(any(), any()))
                .thenReturn(CompletableFuture.failedFuture(new IllegalArgumentException("Empty file")));

        // *ожидаемые результаты*
        // проверяем что нет ошибки 500
        mockMvc.perform(multipart("/home/upload")
                        .file(emptyFile)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));
    }

    @Test
    @Description("Тест проверяет скачивание существующего файла. " +
            "Используется техника тест-дизайна 'Классы эквивалентности' для проверки работы системы " +
            "с корректными данными. Тест важен для обеспечения корректного скачивания файлов.")
    @AllureId("UI-DOWNLOAD-001")
    @Step("Проверка скачивания существующего файла")
    void downloadFile() throws Exception {
        Allure.step("Подготовка данных: создание тестового файла");
        // Создание тестового объекта-файла
        File file = new File();
        file.setFileName("test.txt");
        file.setFilePath("/directory"); // Полный путь
        file.setContentType("text/plain");
        file.setFileSize(100L);

        // Создание реального временного файла
        Path tempFile = Files.createTempFile("test", ".txt");
        Files.write(tempFile, "test content".getBytes());
        file.setFilePath(tempFile.toString());

        Allure.step("Настройка мока для получения файла");
        // *условия выполнения*
        when(fileService.getFileById(1L)).thenReturn(file);

        Allure.step("Выполнение запроса на скачивание файла");
        mockMvc.perform(get("/home/download/1"))
                .andExpect(status().isOk())      // 200
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION,
                        containsString("attachment")))               // нужно скачать
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION,
                        containsString("filename=\"test.txt\"")));

        Allure.step("Очистка временного файла");
        // Удаление временного файл
        Files.deleteIfExists(tempFile);
    }


    @Test
    @Description("Тест проверяет скачивание несуществующего файла. " +
            "Используется техника тест-дизайна 'Граничные значения' для проверки работы системы " +
            "с некорректными данными. Тест важен для обеспечения корректной обработки ошибок при " +
            "попытке скачать несуществующий файл.")
    @AllureId("UI-DOWNLOAD-002")
    @Step("Проверка скачивания несуществующего файла")
    void downloadNoFile() throws Exception {
        Allure.step("Подготовка данных: настройка мока для несуществующего файла");
        Long fileId = 99L;

        // Мокируем сервис, чтобы он выбросил исключение
        when(fileService.getFileById(fileId)).thenThrow(new RuntimeException("File not found"));

        Allure.step("Выполнение запроса на скачивание несуществующего файла");
        mockMvc.perform(get("/home/download/{id}", fileId))
                .andExpect(status().isInternalServerError())  // 500, так как срабатывает GlobalExceptionHandler
                .andExpect(content().string("File not found"));
    }

    @Test
    @Description("Тест проверяет удаление файла. " +
            "Используется техника тест-дизайна 'Классы эквивалентности' для проверки работы системы " +
            "с корректными данными. Тест важен для обеспечения корректного удаления файлов.")
    @AllureId("UI-DELETE-001")
    @Step("Проверка удаления файла")
    void deleteFile() throws Exception {
        Allure.step("Подготовка данных: настройка мока для успешного удаления файла");
        Long fileId = 1L;

        when(fileService.deleteFile(fileId))
                .thenReturn(CompletableFuture.completedFuture(null));

        Allure.step("Выполнение запроса на удаление файла");
        // *ожидаемые результаты*
        // POST-запрос на удаление
        mockMvc.perform(post("/home/delete/1")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection()) //перенаправление на /home
                .andExpect(redirectedUrl("/home"));
    }

    @Test
    @Description("Тест проверяет удаление несуществующего файла. " +
            "Используется техника тест-дизайна 'Граничные значения' для проверки работы системы " +
            "с некорректными данными. Тест важен для обеспечения корректной обработки ошибок при " +
            "попытке удалить несуществующий файл.")
    @AllureId("UI-DELETE-002")
    @Step("Проверка удаления несуществующего файла")
    void DeleteNoFile() throws Exception {
        Allure.step("Подготовка данных: настройка мока для несуществующего файла");
        Long FileId = 99L;
        // Мокируем исключение при удалении несуществующего файла
        doThrow(new RuntimeException("File not found."))
                .when(fileService).deleteFile(FileId);

        Allure.step("Выполнение запроса на удаление несуществующего файла");
        mockMvc.perform(post("/home/delete/{id}", FileId)
                        .with(csrf()))
                .andExpect(status().isInternalServerError())  // 500, так как срабатывает GlobalExceptionHandler
                .andExpect(content().string("File not found."));

        Allure.step("Проверка вызова сервиса удаления");
        // Проверяем запуск сервиса
        verify(fileService).deleteFile(FileId);
    }

    @Test
    @Description("Тест проверяет отображение списка файлов. " +
            "Используется техника тест-дизайна 'Классы эквивалентности' для проверки работы системы " +
            "с корректными данными. Тест важен для обеспечения корректного отображения списка файлов.")
    @AllureId("UI-LIST-001")
    @Step("Проверка отображения списка файлов")
    void fileList() throws Exception {
        Allure.step("Подготовка данных: создание списка файлов");
        File file1 = new File();
        file1.setFileName("file1.txt");
        file1.setFileSize(100L);

        File file2 = new File();
        file2.setFileName("file2.txt");
        file2.setFileSize(100L);

        File file3 = new File();
        file3.setFileName("file3.txt");
        file3.setFileSize(100L);

        List<File> files = Arrays.asList(file1, file2, file3);

        when(fileService.getFilesByUser(any())).thenReturn(files);

        Allure.step("Выполнение запроса на получение списка файлов");
        mockMvc.perform(get("/home"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("files", files));
    }

    @Test
    @Description("Тест проверяет загрузку файла без CSRF токена. " +
            "Используется техника тест-дизайна 'Граничные значения' для проверки работы системы " +
            "без CSRF защиты. Тест важен для обеспечения безопасности системы при отсутствии CSRF " +
            "защиты.")
    @AllureId("UI-SECURITY-001")
    @Step("Проверка загрузки файла без CSRF токена")
    void uploadWithoutCsrf() throws Exception {
        Allure.step("Подготовка данных: создание тестового файла");
        MockMultipartFile file = new MockMultipartFile(
                "file", "test.txt", "text/plain", "content".getBytes()
        );

        Allure.step("Выполнение запроса на загрузку файла без CSRF токена");
        mockMvc.perform(multipart("/home/upload").file(file))
                .andExpect(status().isForbidden()); //403
    }

    @Test
    @Description("Тест проверяет удаление файла без CSRF токена. " +
            "Используется техника тест-дизайна 'Граничные значения' для проверки работы системы " +
            "без CSRF защиты. Тест важен для обеспечения безопасности системы при отсутствии CSRF " +
            "защиты.")
    @AllureId("UI-SECURITY-002")
    @Step("Проверка удаления файла без CSRF токена")
    void deleteWithoutCsrf() throws Exception {
        Allure.step("Выполнение запроса на удаление файла без CSRF токена");
        mockMvc.perform(post("/home/delete/1"))
                .andExpect(status().isForbidden());  //403
    }

    @Test
    @Description("Тест проверяет доступ к домашней странице без аутентификации. " +
            "Используется техника тест-дизайна 'Граничные значения' для проверки работы системы " +
            "без аутентификации. Тест важен для обеспечения безопасности системы при отсутствии " +
            "аутентификации.")
    @AllureId("UI-SECURITY-003")
    @Step("Проверка доступа к домашней странице без аутентификации")
    void homePageWithoutAuthentication() throws Exception {
        Allure.step("Симуляция неаутентифицированного пользователя");
        // симуляция неаутентифицированного пользователя
        SecurityContextHolder.clearContext();

        Allure.step("Выполнение запроса на домашнюю страницу без аутентификации");
        mockMvc.perform(get("/home"))
                .andExpect(status().is3xxRedirection()) // Должен перенаправить на логин
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @Description("Тест проверяет загрузку файла с ошибкой IO. " +
            "Используется техника тест-дизайна 'Граничные значения' для проверки работы системы " +
            "с ошибками IO. Тест важен для обеспечения корректной обработки ошибок при загрузке файлов.")
    @AllureId("UI-ERROR-001")
    @Step("Проверка загрузки файла с ошибкой IO")
    void uploadFileWithIOException() throws Exception {
        Allure.step("Подготовка данных: создание тестового файла");
        MockMultipartFile file = new MockMultipartFile(
                "file", "large.txt", "text/plain", "large content".getBytes()
        );

        Allure.step("Настройка мока для ошибки IO при загрузке файла");
        // Симулируем IOException при сохранении файла
        when(fileService.addFile(any(), any()))
                .thenReturn(CompletableFuture.failedFuture(new IOException("Disk full")));

        Allure.step("Выполнение запроса на загрузку файла с ошибкой IO");
        mockMvc.perform(multipart("/home/upload")
                        .file(file)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));
    }

    @Test
    @Description("Тест проверяет отображение пустого списка файлов. " +
            "Используется техника тест-дизайна 'Классы эквивалентности' для проверки работы системы " +
            "с пустым списком файлов. Тест важен для обеспечения корректного отображения пустого " +
            "списка файлов.")
    @AllureId("UI-LIST-002")
    @Step("Проверка отображения пустого списка файлов")
    void emptyFileList() throws Exception {
        Allure.step("Подготовка данных: настройка мока для пустого списка файлов");
        when(fileService.getFilesByUser(any())).thenReturn(Collections.emptyList());

        Allure.step("Выполнение запроса на получение пустого списка файлов");
        mockMvc.perform(get("/home"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(model().attribute("files", Collections.emptyList())) // Пустой список
                .andExpect(model().attributeDoesNotExist("error")); // Нет ошибок
    }

}
