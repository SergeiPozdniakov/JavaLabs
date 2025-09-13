package com.clouds.cloud_sprint;

import com.clouds.cloud_sprint.model.File;
import com.clouds.cloud_sprint.model.Users;
import com.clouds.cloud_sprint.services.UserService;
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

    // --- Группа 1: Регистрация и Аутентификация ---

    @Test
    @Order(1)
    @DisplayName("Сценарий 1: Успешная регистрация нового пользователя")
    void testSuccessfulUserRegistration() throws Exception { // <-- Убран CapturedOutput, так как он не нужен
        String username = "newuser";
        String password = "password123";
        String firstName = "John";
        String lastName = "Doe";

        mockMvc.perform(MockMvcRequestBuilders.post("/signup")
                        .param("username", username)
                        .param("password", password)
                        .param("firstName", firstName)
                        .param("lastName", lastName)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        // Проверка, что пользователь создан в БД
        Users createdUser = userRepository.findByUsername(username).orElse(null);
        assertThat(createdUser).isNotNull();
        assertThat(createdUser.getUsername()).isEqualTo(username);
        assertThat(createdUser.getFirstName()).isEqualTo(firstName);
        assertThat(createdUser.getLastName()).isEqualTo(lastName);

        // Проверка, что директория пользователя создана
        Path userDir = Path.of(createdUser.getBaseFolderPath());
        assertThat(Files.exists(userDir)).isTrue();
        assertThat(Files.isDirectory(userDir)).isTrue();

        // Примечание: Сообщение "Регистрация успешна!" добавляется в модель, а не в лог.
        // Поэтому проверка через CapturedOutput была удалена, так как ненадежна.
    }

    @Test
    @Order(2)
    @DisplayName("Сценарий 2: Попытка регистрации с существующим именем пользователя")
    void testRegistrationWithExistingUsername() throws Exception {
        // Создаем первого пользователя через сервис (чтобы baseFolderPath был установлен)
        Users existingUser = new Users();
        existingUser.setUsername("existinguser");
        existingUser.setPassword("password123");
        existingUser.setFirstName("Jane");
        existingUser.setLastName("Smith");
        userService.createUser(existingUser); // <-- Используем сервис!

        // Пытаемся создать второго с тем же именем через контроллер
        mockMvc.perform(MockMvcRequestBuilders.post("/signup")
                        .param("username", "existinguser") // то же имя
                        .param("password", "newpassword")
                        .param("firstName", "New")
                        .param("lastName", "User")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("signup"))
                .andExpect(model().attribute("error", "Пользователь с таким логином уже существует"));
    }

    @Test
    @Order(3)
    @DisplayName("Сценарий 3: Успешный вход в систему")
    void testSuccessfulLogin() throws Exception {
        // Создаем пользователя через сервис
        Users user = new Users();
        user.setUsername("testuser");
        user.setPassword("password"); // Сервис сам захеширует
        user.setFirstName("Test");
        user.setLastName("User");
        userService.createUser(user); // <-- Ключевая строка!

        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .param("username", "testuser")
                        .param("password", "password")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));
    }

    @Test
    @Order(4)
    @DisplayName("Сценарий 4: Неудачный вход в систему (неверный пароль)")
    void testFailedLoginWithWrongPassword() throws Exception {
        // Создаем пользователя через сервис
        Users user = new Users();
        user.setUsername("testuser");
        user.setPassword("password"); // Сервис сам захеширует
        user.setFirstName("Test");
        user.setLastName("User");
        userService.createUser(user); // <-- Ключевая строка!

        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .param("username", "testuser")
                        .param("password", "wrongpassword") // Неверный пароль
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error"));
    }

    // --- Группа 2: Управление Файлами ---

    @Test
    @Order(5)
    @DisplayName("Сценарий 5: Попытка доступа к несуществующему файлу")
    @WithMockUser(username = "nonexistent", authorities = "USER")
    void testAccessNonExistentFile() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/home/download/9999"))
                .andExpect(status().is5xxServerError()); // Ожидаем ошибку, так как файла нет
    }

    @Test
    @Order(6)
    @DisplayName("Сценарий 6: Скачивание ранее загруженного файла")
    @WithMockUser(username = "downloader", password = "password", authorities = "USER")
    void testFileDownload() throws Exception {
        // Создаем пользователя через сервис
        Users user = new Users();
        user.setUsername("downloader");
        user.setPassword("password"); // Сервис сам захеширует
        user.setFirstName("Downloader");
        user.setLastName("Test");
        userService.createUser(user); // <-- Ключевая строка!

        // Создаем файл вручную для теста скачивания
        File file = new File();
        file.setFileName("download_me.txt");
        file.setContentType("text/plain");
        file.setFileSize(19);
        file.setFilePath(user.getBaseFolderPath() + "/download_me.txt");
        file.setUser(user);

        // Создаем файл на диске
        Path filePath = Path.of(file.getFilePath());
        Files.createDirectories(filePath.getParent());
        Files.writeString(filePath, "Content for download");

        file = fileRepository.save(file);

        // Выполняем GET-запрос на скачивание
        mockMvc.perform(MockMvcRequestBuilders.get("/home/download/" + file.getId()))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "attachment; filename=\"download_me.txt\""))
                .andExpect(content().string("Content for download"));
    }

    @Test
    @Order(7)
    @DisplayName("Сценарий 7: Удаление файла")
    @WithMockUser(username = "deleter", password = "password", authorities = "USER")
    void testFileDelete() throws Exception {
        // Создаем пользователя через сервис
        Users user = new Users();
        user.setUsername("deleter");
        user.setPassword("password"); // Сервис сам захеширует
        user.setFirstName("Deleter");
        user.setLastName("Test");
        userService.createUser(user); // <-- Ключевая строка!

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

        Long fileId = file.getId();

        mockMvc.perform(MockMvcRequestBuilders.post("/home/delete/" + fileId)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));

        // Проверка, что файл удален из БД
        assertThat(fileRepository.findById(fileId)).isEmpty();

        // Проверка, что файл удален с диска
        assertThat(Files.exists(filePath)).isFalse();
    }

    @Test
    @Order(8)
    @DisplayName("Сценарий 8: Попытка доступа к защищенным ресурсам без аутентификации")
    void testAccessProtectedResourcesWithoutAuth() throws Exception {
        // Попытка доступа к /home
        mockMvc.perform(MockMvcRequestBuilders.get("/home"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));

        // Попытка доступа к /home/upload
        mockMvc.perform(MockMvcRequestBuilders.post("/home/upload")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));

        // Попытка доступа к /home/download/1
        mockMvc.perform(MockMvcRequestBuilders.get("/home/download/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));

        // Попытка доступа к /home/delete/1
        mockMvc.perform(MockMvcRequestBuilders.post("/home/delete/1")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    // --- Группа 3: Безопасность и Сессии ---

    @Test
    @Order(9)
    @DisplayName("Сценарий 9: Доступ к /home запрещен после инвалидации сессии")
    void testProtectedEndpointRequiresAuthAfterSessionInvalidation() throws Exception {
        // Создаем пользователя в БД
        Users user = new Users();
        user.setUsername("sessionuser");
        user.setPassword("password");
        user.setFirstName("Session");
        user.setLastName("User");
        userService.createUser(user);

        // ЯВНО создаем сессию
        MockHttpSession session = new MockHttpSession();

        // Шаг 1: Выполняем логин и сохраняем сессию
        MvcResult loginResult = mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .param("username", "sessionuser")
                        .param("password", "password")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"))
                .andReturn();

        // Извлекаем сессию из результата логина
        session = (MockHttpSession) loginResult.getRequest().getSession();

        // Шаг 2: Проверяем, что с активной сессией доступ разрешен
        mockMvc.perform(MockMvcRequestBuilders.get("/home")
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("home"));

        // Шаг 3: Инвалидируем сессию — УДАЛЯЕМ атрибут
        session.removeAttribute("SPRING_SECURITY_CONTEXT"); // <-- Вот так правильно!

        mockMvc.perform(MockMvcRequestBuilders.get("/home")
                        .session(session)) // <-- Передаем модифицированную сессию
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    @Order(10)
    @DisplayName("Сценарий 10: Защита от CSRF-атак")
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

        // Отправляем запрос БЕЗ CSRF-токена
        mockMvc.perform(MockMvcRequestBuilders.post("/home/delete/" + file.getId()))
                .andExpect(status().isForbidden()); // Ожидаем 403 Forbidden

        // Проверяем, что файл НЕ был удален
        assertThat(fileRepository.findById(file.getId())).isPresent();
    }

    // --- Группа 4: Масштабируемость и Ошибки ---

    @Test
    @Order(11)
    @DisplayName("Сценарий 11: Регистрация с невалидными данными (пустое имя)")
    void testSignupWithInvalidData() throws Exception {
        // Отправляем POST-запрос на /signup с пустым полем "firstName"
        mockMvc.perform(MockMvcRequestBuilders.post("/signup")
                        .param("username", "validuser")
                        .param("password", "validpassword")
                        .param("firstName", "") // <-- Невалидные данные: пустое имя
                        .param("lastName", "ValidLastName")
                        .with(csrf()))
                .andExpect(status().isOk()) // Ожидаем, что страница перерисуется
                .andExpect(view().name("signup")) // Ожидаем, что останемся на странице регистрации
                .andExpect(model().attributeExists("error")) // <-- Проверяем, что появилось сообщение об ошибке
                .andExpect(model().attribute("error", "Проверьте введенные данные")); // <-- Проверяем текст ошибки
    }
}