package com.clouds.cloud_sprint;

import com.clouds.cloud_sprint.model.File;
import com.clouds.cloud_sprint.model.Users;
import com.clouds.cloud_sprint.services.UserService;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
@Tag("integration")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(OutputCaptureExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CloudSprintApplicationIntegrationTest {

    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("cloud_storage_test")
            .withUsername("testuser")
            .withPassword("testpass");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        // Переопределяем путь к хранилищу файлов на временную директорию для изоляции тестов
        registry.add("app.storage.base-path", () -> System.getProperty("java.io.tmpdir") + "/cloud_storage_test");
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private UserService userService; // <-- Ключевая зависимость для создания пользователей

    private static Path tempStoragePath;

    @BeforeAll
    static void setup() {
        tempStoragePath = Path.of(System.getProperty("java.io.tmpdir"), "cloud_storage_test");
    }

    @BeforeEach
    void beforeEach() {
        // Очищаем БД и файловую систему перед каждым тестом
        fileRepository.deleteAll();
        userRepository.deleteAll();
        try {
            if (Files.exists(tempStoragePath)) {
                Files.walk(tempStoragePath)
                        .sorted((a, b) -> -a.compareTo(b)) // Удаляем сначала файлы, потом директории
                        .forEach(path -> {
                            try {
                                Files.delete(path);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        });
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to clean up test directory", e);
        }
    }

    @AfterAll
    static void afterAll() {
        // Опционально: удалить корневую тестовую директорию после всех тестов
        try {
            if (Files.exists(tempStoragePath)) {
                Files.walk(tempStoragePath)
                        .sorted((a, b) -> -a.compareTo(b))
                        .forEach(path -> {
                            try {
                                Files.delete(path);
                            } catch (IOException e) {
                                // Игнорируем ошибки при удалении, так как директория временная
                            }
                        });
            }
        } catch (IOException e) {
            // Игнорируем
        }
    }

    @Test
    void testDatabaseConnection() {
        assertThat(postgres.isRunning()).isTrue();
        System.out.println("DB URL: " + postgres.getJdbcUrl());
    }

    // --- Группа 1: Регистрация и Аутентификация ---

    @Test
    @Order(1)
    @DisplayName("Сценарий 1: Успешная регистрация нового пользователя")
    @Epic("Аутентификация")
    @Feature("Регистрация пользователей")
    @Story("Успешная регистрация нового пользователя")
    @Owner("Команда разработки CloudSprint")
    @Severity(SeverityLevel.CRITICAL)
    @AllureId("AUTH-REG-001")
    @Description("Тест проверяет успешную регистрацию нового пользователя с валидными данными. " +
            "Цель: убедиться, что система корректно обрабатывает регистрацию нового пользователя и создает необходимые ресурсы. " +
            "Используется техника тест-дизайна 'Классы эквивалентности' для проверки работы системы " +
            "с корректными данными. Тест важен для обеспечения корректной регистрации новых пользователей в системе.")
    void testSuccessfulUserRegistration() throws Exception {
        String username = "newuser";
        String password = "password123";
        String firstName = "John";
        String lastName = "Doe";

        Allure.step("Подготовка данных для регистрации: создание валидных пользовательских данных");
        Allure.addAttachment("Регистрационные данные", "Имя пользователя: " + username +
                ", Пароль: " + password + ", Имя: " + firstName + ", Фамилия: " + lastName);

        Allure.step("Выполнение запроса на регистрацию");
        mockMvc.perform(MockMvcRequestBuilders.post("/signup")
                        .param("username", username)
                        .param("password", password)
                        .param("firstName", firstName)
                        .param("lastName", lastName)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        Allure.step("Проверка создания пользователя в базе данных");
        Users createdUser = userRepository.findByUsername(username).orElse(null);
        assertThat(createdUser).isNotNull();
        assertThat(createdUser.getUsername()).isEqualTo(username);
        assertThat(createdUser.getFirstName()).isEqualTo(firstName);
        assertThat(createdUser.getLastName()).isEqualTo(lastName);
        Allure.addAttachment("Созданный пользователь", "ID: " + createdUser.getId() +
                ", Логин: " + createdUser.getUsername() + ", Имя: " + createdUser.getFirstName() +
                ", Фамилия: " + createdUser.getLastName());

        Allure.step("Проверка создания директории пользователя в файловой системе");
        Path userDir = Path.of(createdUser.getBaseFolderPath());
        assertThat(Files.exists(userDir)).isTrue();
        assertThat(Files.isDirectory(userDir)).isTrue();
        Allure.addAttachment("Директория пользователя", "Путь: " + userDir.toString() +
                ", Существует: " + Files.exists(userDir) + ", Это директория: " + Files.isDirectory(userDir));
    }

    @Test
    @Order(2)
    @DisplayName("Сценарий 2: Попытка регистрации с существующим именем пользователя")
    @Epic("Аутентификация")
    @Feature("Регистрация пользователей")
    @Story("Попытка регистрации с существующим именем пользователя")
    @Owner("Команда разработки CloudSprint")
    @Severity(SeverityLevel.CRITICAL)
    @AllureId("AUTH-REG-002")
    @Description("Тест проверяет обработку ошибки при попытке зарегистрировать пользователя с уже существующим именем. " +
            "Цель: убедиться, что система корректно обрабатывает ситуацию дублирования логина и возвращает понятное сообщение об ошибке. " +
            "Используется техника тест-дизайна 'Граничные значения' для проверки работы системы " +
            "с некорректными данными (повторяющееся имя пользователя). Тест важен для обеспечения корректной обработки ошибок при регистрации.")
    void testRegistrationWithExistingUsername() throws Exception {
        // Создаем первого пользователя через сервис (чтобы baseFolderPath был установлен)
        Users existingUser = new Users();
        existingUser.setUsername("existinguser");
        existingUser.setPassword("password123");
        existingUser.setFirstName("Jane");
        existingUser.setLastName("Smith");
        userService.createUser(existingUser); // <-- Используем сервис!

        Allure.step("Подготовка данных для повторной регистрации с существующим именем");
        Allure.addAttachment("Регистрационные данные", "Имя пользователя: existinguser" +
                ", Новый пароль: newpassword" + ", Новое имя: New" + ", Новая фамилия: User");

        Allure.step("Выполнение запроса на регистрацию с существующим именем");
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/signup")
                        .param("username", "existinguser") // то же имя
                        .param("password", "newpassword")
                        .param("firstName", "New")
                        .param("lastName", "User")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("signup"))
                .andExpect(model().attribute("error", "Пользователь с таким логином уже существует"))
                .andReturn();

        Allure.step("Проверка сообщения об ошибке");
        Allure.addAttachment("Результат регистрации", "Статус: " + result.getResponse().getStatus() +
                ", View: " + result.getModelAndView().getViewName() +
                ", Ошибка: " + result.getModelAndView().getModel().get("error"));
    }

    @Test
    @Order(3)
    @DisplayName("Сценарий 3: Успешный вход в систему")
    @Epic("Аутентификация")
    @Feature("Вход в систему")
    @Story("Успешный вход в систему")
    @Owner("Команда разработки CloudSprint")
    @Severity(SeverityLevel.CRITICAL)
    @AllureId("AUTH-LOGIN-001")
    @Description("Тест проверяет успешный вход в систему с валидными учетными данными. " +
            "Цель: убедиться, что система корректно обрабатывает аутентификацию пользователя и перенаправляет на главную страницу. " +
            "Используется техника тест-дизайна 'Классы эквивалентности' для проверки работы системы " +
            "с корректными данными. Тест важен для обеспечения корректной аутентификации пользователей.")
    void testSuccessfulLogin() throws Exception {
        // Создаем пользователя через сервис
        Users user = new Users();
        user.setUsername("testuser");
        user.setPassword("password"); // Сервис сам захеширует
        user.setFirstName("Test");
        user.setLastName("User");
        userService.createUser(user); // <-- Ключевая строка!

        Allure.step("Подготовка данных для входа в систему");
        Allure.addAttachment("Данные для входа", "Имя пользователя: testuser" + ", Пароль: password");

        Allure.step("Выполнение запроса на вход в систему");
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .param("username", "testuser")
                        .param("password", "password")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"))
                .andReturn();

        Allure.step("Проверка перенаправления на главную страницу");
        Allure.addAttachment("Результат входа", "Статус: " + result.getResponse().getStatus() +
                ", Перенаправление: " + result.getResponse().getRedirectedUrl());
    }

    @Test
    @Order(4)
    @DisplayName("Сценарий 4: Неудачный вход в систему (неверный пароль)")
    @Epic("Аутентификация")
    @Feature("Вход в систему")
    @Story("Неудачный вход в систему (неверный пароль)")
    @Owner("Команда разработки CloudSprint")
    @Severity(SeverityLevel.CRITICAL)
    @AllureId("AUTH-LOGIN-002")
    @Description("Тест проверяет обработку ошибки при входе в систему с неверным паролем. " +
            "Цель: убедиться, что система корректно обрабатывает ситуацию неверного пароля и перенаправляет на страницу входа с сообщением об ошибке. " +
            "Используется техника тест-дизайна 'Граничные значения' для проверки работы системы " +
            "с некорректными данными (неверный пароль). Тест важен для обеспечения безопасности системы и предотвращения несанкционированного доступа.")
    void testFailedLoginWithWrongPassword() throws Exception {
        // Создаем пользователя через сервис
        Users user = new Users();
        user.setUsername("testuser");
        user.setPassword("password"); // Сервис сам захеширует
        user.setFirstName("Test");
        user.setLastName("User");
        userService.createUser(user); // <-- Ключевая строка!

        Allure.step("Подготовка данных для неудачного входа в систему");
        Allure.addAttachment("Данные для входа", "Имя пользователя: testuser" +
                ", Неверный пароль: wrongpassword");

        Allure.step("Выполнение запроса на вход в систему с неверным паролем");
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .param("username", "testuser")
                        .param("password", "wrongpassword") // Неверный пароль
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error"))
                .andReturn();

        Allure.step("Проверка перенаправления на страницу входа с ошибкой");
        Allure.addAttachment("Результат входа", "Статус: " + result.getResponse().getStatus() +
                ", Перенаправление: " + result.getResponse().getRedirectedUrl());
    }

    // --- Группа 2: Управление Файлами ---

    @Test
    @Order(5)
    @DisplayName("Сценарий 5: Попытка доступа к несуществующему файлу")
    @Epic("Файловые операции")
    @Feature("Управление файлами")
    @Story("Попытка доступа к несуществующему файлу")
    @Owner("Команда разработки CloudSprint")
    @Severity(SeverityLevel.CRITICAL)
    @AllureId("FILE-ACCESS-001")
    @Description("Тест проверяет обработку ошибки при попытке доступа к несуществующему файлу. " +
            "Цель: убедиться, что система корректно обрабатывает ситуацию отсутствия файла и возвращает понятное сообщение об ошибке. " +
            "Используется техника тест-дизайна 'Граничные значения' для проверки работы системы " +
            "с некорректными данными (ID файла, которого нет в системе). Тест важен для обеспечения корректной обработки ошибок и предоставления пользователю понятного сообщения об ошибке.")
    @WithMockUser(username = "nonexistent", authorities = "USER")
    void testAccessNonExistentFile() throws Exception {
        Allure.step("Подготовка данных: аутентифицированный пользователь");
        Allure.addAttachment("Аутентифицированный пользователь", "Имя пользователя: nonexistent" +
                ", Права: USER");

        Allure.step("Выполнение запроса на доступ к несуществующему файлу");
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/home/download/9999"))
                .andExpect(status().is5xxServerError())
                .andReturn();

        Allure.step("Проверка ответа сервера");
        Allure.addAttachment("Ответ сервера", "Статус: " + result.getResponse().getStatus() +
                ", Содержимое: " + result.getResponse().getContentAsString());
    }

    @Test
    @Order(6)
    @DisplayName("Сценарий 6: Скачивание ранее загруженного файла")
    @Epic("Файловые операции")
    @Feature("Управление файлами")
    @Story("Скачивание ранее загруженного файла")
    @Owner("Команда разработки CloudSprint")
    @Severity(SeverityLevel.CRITICAL)
    @AllureId("FILE-DOWNLOAD-001")
    @Description("Тест проверяет корректное скачивание ранее загруженного файла. " +
            "Цель: убедиться, что система корректно обрабатывает запрос на скачивание файла и возвращает правильные HTTP заголовки и содержимое. " +
            "Используется техника тест-дизайна 'Классы эквивалентности' для проверки работы системы " +
            "с корректными данными. Тест важен для обеспечения корректной работы механизма скачивания файлов.")
    @WithMockUser(username = "downloader", password = "password", authorities = "USER")
    void testFileDownload() throws Exception {
        // Создаем пользователя через сервис
        Users user = new Users();
        user.setUsername("downloader");
        user.setPassword("password"); // Сервис сам захеширует
        user.setFirstName("Downloader");
        user.setLastName("Test");
        userService.createUser(user); // <-- Ключевая строка!

        Allure.step("Подготовка данных: создание тестового файла");
        File file = new File();
        file.setFileName("download_me.txt");
        file.setContentType("text/plain");
        file.setFileSize(19);
        file.setFilePath(user.getBaseFolderPath() + "/download_me.txt");
        file.setUser(user);

        Path filePath = Path.of(file.getFilePath());
        Files.createDirectories(filePath.getParent());
        Files.writeString(filePath, "Content for download");
        file = fileRepository.save(file);

        Allure.addAttachment("Созданный файл", "Имя: " + file.getFileName() +
                ", Путь: " + file.getFilePath() +
                ", Размер: " + file.getFileSize() + " байт");

        Allure.step("Выполнение запроса на скачивание файла");
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/home/download/" + file.getId()))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "attachment; filename=\"download_me.txt\""))
                .andExpect(content().string("Content for download"))
                .andReturn();

        Allure.step("Проверка ответа сервера");
        Allure.addAttachment("Ответ сервера", "Статус: " + result.getResponse().getStatus() +
                ", Content-Disposition: " + result.getResponse().getHeader("Content-Disposition") +
                ", Содержимое: " + result.getResponse().getContentAsString());
    }

    @Test
    @Order(7)
    @DisplayName("Сценарий 7: Удаление файла")
    @Epic("Файловые операции")
    @Feature("Управление файлами")
    @Story("Удаление файла")
    @Owner("Команда разработки CloudSprint")
    @Severity(SeverityLevel.CRITICAL)
    @AllureId("FILE-DELETE-001")
    @Description("Тест проверяет корректное удаление файла из системы. " +
            "Цель: убедиться, что система корректно обрабатывает запрос на удаление файла и удаляет файл как из базы данных, так и из файловой системы. " +
            "Используется техника тест-дизайна 'Классы эквивалентности' для проверки работы системы " +
            "с корректными данными. Тест важен для обеспечения корректного удаления файлов и удаления файлов из файловой системы.")
    @WithMockUser(username = "deleter", password = "password", authorities = "USER")
    void testFileDelete() throws Exception {
        // Создаем пользователя через сервис
        Users user = new Users();
        user.setUsername("deleter");
        user.setPassword("password"); // Сервис сам захеширует
        user.setFirstName("Deleter");
        user.setLastName("Test");
        userService.createUser(user); // <-- Ключевая строка!

        Allure.step("Подготовка данных: создание тестового файла для удаления");
        File file = new File();
        file.setFileName("delete_me.txt");
        file.setContentType("text/plain");
        file.setFileSize(11);
        file.setFilePath(user.getBaseFolderPath() + "/delete_me.txt");
        file.setUser(user);

        Path filePath = Path.of(file.getFilePath());
        Files.createDirectories(filePath.getParent());
        Files.writeString(filePath, "To be deleted");
        file = fileRepository.save(file);

        Allure.addAttachment("Созданный файл", "Имя: " + file.getFileName() +
                ", Путь: " + file.getFilePath() +
                ", Размер: " + file.getFileSize() + " байт");

        Long fileId = file.getId();

        Allure.step("Выполнение запроса на удаление файла");
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/home/delete/" + fileId)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"))
                .andReturn();

        Allure.step("Проверка удаления файла из базы данных");
        assertThat(fileRepository.findById(fileId)).isEmpty();
        Allure.addAttachment("Результат проверки БД", "Файл с ID " + fileId + " удален из базы данных: true");

        Allure.step("Проверка удаления файла из файловой системы");
        assertThat(Files.exists(filePath)).isFalse();
        Allure.addAttachment("Результат проверки файловой системы", "Файл по пути " + filePath + " удален: true");
    }

    @Test
    @Order(8)
    @DisplayName("Сценарий 8: Попытка доступа к защищенным ресурсам без аутентификации")
    @Epic("Безопасность")
    @Feature("Аутентификация")
    @Story("Попытка доступа к защищенным ресурсам без аутентификации")
    @Owner("Команда разработки CloudSprint")
    @Severity(SeverityLevel.CRITICAL)
    @AllureId("SECURITY-ACCESS-001")
    @Description("Тест проверяет защиту защищенных ресурсов системы от неаутентифицированных пользователей. " +
            "Цель: убедиться, что система корректно перенаправляет неаутентифицированных пользователей на страницу входа. " +
            "Используется техника тест-дизайна 'Граничные значения' для проверки работы системы " +
            "без аутентификации. Тест важен для обеспечения безопасности системы и предотвращения несанкционированного доступа к защищенным ресурсам.")
    void testAccessProtectedResourcesWithoutAuth() throws Exception {
        Allure.step("Попытка доступа к /home без аутентификации");
        MvcResult resultHome = mockMvc.perform(MockMvcRequestBuilders.get("/home"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"))
                .andReturn();

        Allure.addAttachment("Результат доступа к /home", "Статус: " + resultHome.getResponse().getStatus() +
                ", Перенаправление: " + resultHome.getResponse().getRedirectedUrl());

        Allure.step("Попытка доступа к /home/upload без аутентификации");
        MvcResult resultUpload = mockMvc.perform(MockMvcRequestBuilders.post("/home/upload")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"))
                .andReturn();

        Allure.addAttachment("Результат доступа к /home/upload", "Статус: " + resultUpload.getResponse().getStatus() +
                ", Перенаправление: " + resultUpload.getResponse().getRedirectedUrl());

        Allure.step("Попытка доступа к /home/download/1 без аутентификации");
        MvcResult resultDownload = mockMvc.perform(MockMvcRequestBuilders.get("/home/download/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"))
                .andReturn();

        Allure.addAttachment("Результат доступа к /home/download/1", "Статус: " + resultDownload.getResponse().getStatus() +
                ", Перенаправление: " + resultDownload.getResponse().getRedirectedUrl());

        Allure.step("Попытка доступа к /home/delete/1 без аутентификации");
        MvcResult resultDelete = mockMvc.perform(MockMvcRequestBuilders.post("/home/delete/1")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"))
                .andReturn();

        Allure.addAttachment("Результат доступа к /home/delete/1", "Статус: " + resultDelete.getResponse().getStatus() +
                ", Перенаправление: " + resultDelete.getResponse().getRedirectedUrl());
    }

    // --- Группа 3: Безопасность и Сессии ---

    @Test
    @Order(9)
    @DisplayName("Сценарий 9: Доступ к /home запрещен после инвалидации сессии")
    @Epic("Безопасность")
    @Feature("Сессии и аутентификация")
    @Story("Доступ к /home запрещен после инвалидации сессии")
    @Owner("Команда разработки CloudSprint")
    @Severity(SeverityLevel.CRITICAL)
    @AllureId("SECURITY-SESSION-001")
    @Description("Тест проверяет корректную работу системы при инвалидации сессии пользователя. " +
            "Цель: убедиться, что система корректно обрабатывает инвалидацию сессии и запрещает доступ к защищенным ресурсам. " +
            "Используется техника тест-дизайна 'Граничные значения' для проверки работы системы " +
            "с инвалидированной сессией. Тест важен для обеспечения безопасности системы и предотвращения доступа к защищенным ресурсам после выхода из системы.")
    void testProtectedEndpointRequiresAuthAfterSessionInvalidation() throws Exception {
        // Создаем пользователя в БД
        Users user = new Users();
        user.setUsername("sessionuser");
        user.setPassword("password");
        user.setFirstName("Session");
        user.setLastName("User");
        userService.createUser(user);

        Allure.step("Создание сессии пользователя");
        MockHttpSession session = new MockHttpSession();

        Allure.step("Выполнение логина и сохранение сессии");
        MvcResult loginResult = mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .param("username", "sessionuser")
                        .param("password", "password")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"))
                .andReturn();

        // Извлекаем сессию из результата логина
        session = (MockHttpSession) loginResult.getRequest().getSession();

        Allure.step("Проверка доступа к /home с активной сессией");
        MvcResult resultActive = mockMvc.perform(MockMvcRequestBuilders.get("/home")
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andReturn();

        Allure.addAttachment("Доступ с активной сессией", "Статус: " + resultActive.getResponse().getStatus() +
                ", View: " + resultActive.getModelAndView().getViewName());

        Allure.step("Инвалидация сессии");
        session.removeAttribute("SPRING_SECURITY_CONTEXT"); // <-- Вот так правильно!

        Allure.step("Проверка доступа к /home после инвалидации сессии");
        MvcResult resultInvalidated = mockMvc.perform(MockMvcRequestBuilders.get("/home")
                        .session(session)) // <-- Передаем модифицированную сессию
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"))
                .andReturn();

        Allure.addAttachment("Доступ с инвалидированной сессией", "Статус: " + resultInvalidated.getResponse().getStatus() +
                ", Перенаправление: " + resultInvalidated.getResponse().getRedirectedUrl());
    }

    @Test
    @Order(10)
    @DisplayName("Сценарий 10: Защита от CSRF-атак")
    @Epic("Безопасность")
    @Feature("CSRF защита")
    @Story("Защита от CSRF-атак")
    @Owner("Команда разработки CloudSprint")
    @Severity(SeverityLevel.CRITICAL)
    @AllureId("SECURITY-CSRF-001")
    @Description("Тест проверяет корректную работу защиты от CSRF-атак. " +
            "Цель: убедиться, что система корректно обрабатывает запросы без CSRF-токена и возвращает ошибку 403 Forbidden. " +
            "Используется техника тест-дизайна 'Граничные значения' для проверки работы системы " +
            "без CSRF-токена. Тест важен для обеспечения безопасности системы и предотвращения межсайтовых запросов (CSRF-атак).")
    @WithMockUser(username = "csrfuser", password = "password", authorities = "USER")
    void testCsrfProtection() throws Exception {
        // Создаем пользователя через сервис
        Users user = new Users();
        user.setUsername("csrfuser");
        user.setPassword("password"); // Сервис сам захеширует
        user.setFirstName("CSRF");
        user.setLastName("User");
        userService.createUser(user); // <-- Ключевая строка!

        File file = new File();
        file.setFileName("csrf_test.txt");
        file.setContentType("text/plain");
        file.setFileSize(0);
        file.setFilePath("dummy_path");
        file.setUser(user);
        file = fileRepository.save(file);

        Allure.step("Подготовка данных: создание тестового файла");
        Allure.addAttachment("Созданный файл", "Имя: " + file.getFileName() +
                ", ID: " + file.getId() +
                ", Тип контента: " + file.getContentType());

        Allure.step("Выполнение запроса на удаление файла без CSRF-токена");
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/home/delete/" + file.getId()))
                .andExpect(status().isForbidden()) // Ожидаем 403 Forbidden
                .andReturn();

        Allure.step("Проверка, что файл не был удален");
        assertThat(fileRepository.findById(file.getId())).isPresent();
        Allure.addAttachment("Результат проверки БД", "Файл с ID " + file.getId() + " все еще существует в базе данных: true");
    }

    // --- Группа 4: Масштабируемость и Ошибки ---

    @Test
    @Order(11)
    @DisplayName("Сценарий 11: Регистрация с невалидными данными (пустое имя)")
    @Epic("Аутентификация")
    @Feature("Регистрация пользователей")
    @Story("Регистрация с невалидными данными (пустое имя)")
    @Owner("Команда разработки CloudSprint")
    @Severity(SeverityLevel.CRITICAL)
    @AllureId("AUTH-REG-003")
    @Description("Тест проверяет обработку ошибок при регистрации с невалидными данными (пустое имя). " +
            "Цель: убедиться, что система корректно обрабатывает невалидные данные и возвращает понятное сообщение об ошибке. " +
            "Используется техника тест-дизайна 'Граничные значения' для проверки работы системы " +
            "с некорректными данными (пустое имя). Тест важен для обеспечения корректной валидации входных данных и предоставления пользователю понятных сообщений об ошибках.")
    void testSignupWithInvalidData() throws Exception {
        Allure.step("Подготовка данных для регистрации с невалидными данными");
        Allure.addAttachment("Регистрационные данные", "Имя пользователя: validuser" +
                ", Пароль: validpassword" + ", Пустое имя" + ", Фамилия: ValidLastName");

        Allure.step("Выполнение запроса на регистрацию с невалидными данными");
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/signup")
                        .param("username", "validuser")
                        .param("password", "validpassword")
                        .param("firstName", "") // <-- Невалидные данные: пустое имя
                        .param("lastName", "ValidLastName")
                        .with(csrf()))
                .andExpect(status().isOk()) // Ожидаем, что страница перерисуется
                .andExpect(view().name("signup")) // Ожидаем, что останемся на странице регистрации
                .andExpect(model().attributeExists("error")) // <-- Проверяем, что появилось сообщение об ошибке
                .andExpect(model().attribute("error", "Проверьте введенные данные"))
                .andReturn();

        Allure.step("Проверка сообщения об ошибке");
        Allure.addAttachment("Результат регистрации", "Статус: " + result.getResponse().getStatus() +
                ", View: " + result.getModelAndView().getViewName() +
                ", Ошибка: " + result.getModelAndView().getModel().get("error"));
    }
}