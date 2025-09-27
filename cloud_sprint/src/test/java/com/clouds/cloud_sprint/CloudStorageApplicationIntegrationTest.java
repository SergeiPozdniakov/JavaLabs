package com.clouds.cloud_sprint;

import com.clouds.cloud_sprint.model.File;
import com.clouds.cloud_sprint.model.Users;
import com.clouds.cloud_sprint.security.UserDetailsServiceImpl;
import com.clouds.cloud_sprint.services.UserService;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import com.clouds.cloud_sprint.services.FileService;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class CloudStorageApplicationIntegrationTest {
    private Users testUser;
    private final AtomicLong userCounter = new AtomicLong(0); // Безопасно в многопоточности, нет потери данных

    // Контейнер должен быть статическим
    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("cloud_storage_test")
            .withUsername("testuser")
            .withPassword("testpass")
            .withReuse(true); // для ускорения тестов

    // Динамические свойства должны быть настроены ДО инициализации контекста
    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        // запуск контейнера
        postgres.start();

        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");

        registry.add("spring.jpa.database-platform", () -> "org.hibernate.dialect.PostgreSQLDialect");
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
        registry.add("spring.jpa.properties.hibernate.dialect", () -> "org.hibernate.dialect.PostgreSQLDialect");

        // Файловое хранилище
        registry.add("app.storage.base-path", () ->
                System.getProperty("java.io.tmpdir") + "/cloud_storage_integration_test");
    }

    @MockBean
    private ExternalVersionService externalVersionService;

    @MockBean
    private ExternalFtpService externalFtpService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    FileService fileService;

    private static Path tempStoragePath;

    @BeforeAll
    static void setupAll() {
        tempStoragePath = Path.of(System.getProperty("java.io.tmpdir"),
                "cloud_storage_integration_test");

        try {
            Files.createDirectories(tempStoragePath);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create temp directory", e);
        }
    }

    @BeforeEach
    void setupEach() {

        // Сначала Очистка БД
        if (fileRepository != null) fileRepository.deleteAll();
        if (userRepository != null) userRepository.deleteAll();

        // Очистка файловой системы
        cleanupTestStorage();

        // Настройка заглушек для внешних сервисов
        setupExternalServiceMocks();

        // Базовый пользователь для большинства тестов после очистки
        testUser = createTestUser("base_user_" + System.currentTimeMillis());

    }

    @AfterEach
    void tearDownEach() {
        cleanupTestStorage();
    }

    @AfterAll
    static void tearDownAll() {
        cleanupTestStorage();
        // Останавливаем контейнер после всех тестов
        if (postgres != null && postgres.isRunning()) {
            postgres.stop();
        }
    }

    private static void cleanupTestStorage() {
        try {
            if (Files.exists(tempStoragePath)) {
                Files.walk(tempStoragePath)
                        .sorted((a, b) -> -a.compareTo(b))
                        .forEach(path -> {
                            try {
                                Files.deleteIfExists(path);
                            } catch (Exception e) {
                                System.err.println("Failed to delete: " + path);
                            }
                        });
            }
        } catch (Exception e) {
            System.err.println("Cleanup error: " + e.getMessage());
        }
    }

    private void setupExternalServiceMocks() {
        when(externalVersionService.getLatestVersion()).thenReturn("1.0.0");
        when(externalFtpService.downloadArtifact(anyString())).thenReturn(new byte[]{1, 2, 3, 4, 5});
    }

    // Интерфейсы для заглушек внешних сервисов
    public interface ExternalVersionService {
        String getLatestVersion();
    }

    public interface ExternalFtpService {
        byte[] downloadArtifact(String artifactUrl);
    }


    // Вспомогательные методы для создания тестовых данных
    private Users createTestUser() {
        return createTestUser("test_user_" + System.currentTimeMillis());
    }

    private Users createTestUser(String username) {
        Users user = new Users();
        user.setUsername(username);
        user.setPassword("file_password");
        user.setFirstName("Иван");
        user.setLastName("Петров");
        return userService.createUser(user);
    }

    private Users createUniqueUser() {
        return createTestUser("unique_user_" + userCounter.incrementAndGet());
    }

    // Перегруженные методы создания тестовых файлов
    private MockMultipartFile createTestFile() {
        return createTestFile("integration_test.txt", "Integration test content");
    }

    private MockMultipartFile createTestFile(String filename, String content) {
        return new MockMultipartFile(
                "file",
                filename,
                "text/plain",
                content.getBytes()
        );
    }

    //UserService → Database → FileService → File System → FileRepository → Database
    @Test
    @Order(1)
    @DisplayName("Сценарий 1: Интеграция UserService и FileService")
    @Epic("Управление файлами")
    @Feature("Интеграция сервисов")
    @Story("Взаимодействие UserService и FileService")
    @Owner("Команда разработки CloudSprint")
    @AllureId("INTEGRATION-001")
    @Description("Тест проверяет интеграционное взаимодействие между UserService и FileService. " +
            "Проверяет создание пользователя, загрузку файла и связывание файла с пользователем в БД и файловой системе.")


    void givenUserServiceAndFileService_whenUserUploadsFile_thenFileIsLinkedToUser() throws Exception {
        // ДАНО: Сервисы пользователя и файлов
        // UserService.createUser() → UserRepository.save() → PostgreSQL
        // Users user = createTestUser(); - в Before each
        Allure.step("Создание тестового файла");
        MockMultipartFile file = createTestFile();

        // КОГДА: Пользователь загружает файл
        //Интерфейс FileService ↔ File System
        Allure.step("Загрузка файла через FileService");
        File uploadedFile = fileService.addFile(file, testUser).get(5, TimeUnit.SECONDS);

        // ТОГДА: Проверяем интеграцию через репозитории
        // FileService ↔ Database
        Allure.step("Проверка наличия файла в репозитории");
        List<File> userFiles = fileRepository.findByUser(testUser);

        // Сравнение идёт по ID
        assertThat(userFiles).isNotEmpty();
        assertThat(userFiles).extracting(File::getId).contains(uploadedFile.getId());
        assertThat(userFiles).extracting(File::getFileName).contains("integration_test.txt");

        // И: Файл физически находится в папке пользователя
        //Business Logic ↔ Data Integrity
        Allure.step("Проверка пути файла (содержит имя пользователя)");
        assertThat(uploadedFile.getFilePath()).contains(testUser.getUsername());

        // И: Файл существует в файловой системе
        Allure.step("Проверка существования физического файла");
        Path filePath = Path.of(uploadedFile.getFilePath());
        assertThat(Files.exists(filePath)).isTrue();

        // И: Файл физически находится в папке пользователя
        // Business Logic ↔ Data Integrity
        assertThat(uploadedFile.getFilePath()).contains(testUser.getUsername());

        // ДОБАВИТЬ: Проверку, что UserService создал папку пользователя
        Allure.step("Проверка создания папки пользователя");
        Path userFolderPath = Path.of(testUser.getBaseFolderPath());
        assertThat(Files.exists(userFolderPath)).isTrue();
        assertThat(Files.isDirectory(userFolderPath)).isTrue();

        // ДОБАВИТЬ: Проверку связи пользователь-файл в БД
        Allure.step("Проверка связи пользователь-файл в БД");
        assertThat(uploadedFile.getUser().getId()).isEqualTo(testUser.getId());
        assertThat(uploadedFile.getUser().getUsername()).isEqualTo(testUser.getUsername());
    }

    //FileService.addFile() ↔ FileRepository.save() → PostgreSQL
    //FileService ↔ Файловая система (сохранение физического файла)
    //Связь сущностей в БД: File ↔ User (JPA relationships), операции с БД и файловой системой в одной транзакции
    @Test
    @Order(2)
    @DisplayName("Сценарий 2: Связь файла с пользователем в БД")
    @Epic("Управление файлами")
    @Feature("Связи данных")
    @Story("Связывание файлов с пользователями")
    @Owner("Команда разработки CloudSprint")
    @AllureId("INTEGRATION-002")
    @Description("Тест проверяет корректность установки связей между файлами и пользователями в базе данных. " +
            "Валидирует JPA relationships и целостность внешних ключей.")
    void givenUserAndFileService_whenUploadFile_thenFileLinkedInDatabase() throws Exception {
        Allure.step("Создание тестового файла");
        // ДАНО: Созданный пользователь
        MockMultipartFile file = createTestFile("critical_test.txt", "Critical test content");

        Allure.step("Загрузка файла через FileService");
        // КОГДА: Загружаем файл
        File uploadedFile = fileService.addFile(file, testUser).get(5, TimeUnit.SECONDS);

        Allure.step("Проверка связи файла с пользователем в БД");
        // ТОГДА: Файл связан с пользователем в БД
        List<File> userFiles = fileRepository.findByUser(testUser);
        assertThat(userFiles).hasSize(1);
        assertThat(userFiles.get(0).getId()).isEqualTo(uploadedFile.getId());
        assertThat(userFiles.get(0).getUser().getId()).isEqualTo(testUser.getId());
    }


    //FileService.addFile() ↔ FileRepository ↔ PostgreSQL (многократно)
    //FileService.getFilesByUser() ↔ FileRepository.findByUser() ↔ PostgreSQL
    //FileService ↔ Файловая система (создание физических файлов)
    @Test
    @Order(3)
    @DisplayName("Сценарий 3: Множественная загрузка файлов")
    @Epic("Управление файлами")
    @Feature("Массовые операции")
    @Story("Загрузка нескольких файлов пользователем")
    @Owner("Команда разработки CloudSprint")
    @AllureId("INTEGRATION-003")
    @Description("Тест проверяет возможность пользователя загружать несколько файлов и их корректное хранение." +
            "Проверяет изоляцию данных.")

    void givenTwoUsersWithFiles_whenGetUserFiles_thenStrictIsolationEnforced() throws Exception {

        Allure.step("Создание второго пользователя");
        // ДАНО: Два пользователя с файлами (создаем через UserService)
        Users testUser2 = new Users();
        testUser2.setUsername("testuser2");
        testUser2.setPassword("password2");
        testUser2.setFirstName("Test2");
        testUser2.setLastName("User2");

        testUser2 = userService.createUser(testUser2); // Важно: создаем через сервис!

        Allure.step("Создание тестовых файлов для обоих пользователей");
        MockMultipartFile file1 = createTestFile("file1.txt", "Content 1");
        MockMultipartFile file2 = createTestFile("file2.txt", "Content 2");
        MockMultipartFile fileUser2 = createTestFile("user2file.txt", "User2 content");

        Allure.step("Загрузка файлов для пользователей");
        // КОГДА: Загружаем файлы для обоих пользователей
        fileService.addFile(file1, testUser).get(5, TimeUnit.SECONDS);
        fileService.addFile(file2, testUser).get(5, TimeUnit.SECONDS);
        fileService.addFile(fileUser2, testUser2).get(5, TimeUnit.SECONDS);

        Allure.step("Проверка изоляции данных: количество файлов");
        // ТОГДА: Проверяем изоляцию данных (позитивные проверки)
        List<File> user1Files = fileService.getFilesByUser(testUser);
        List<File> user2Files = fileService.getFilesByUser(testUser2);

        assertThat(user1Files).hasSize(2);
        assertThat(user2Files).hasSize(1);

        Allure.step("Проверка содержания файлов пользователя 1");
        assertThat(user1Files)
                .extracting(File::getFileName)
                .containsExactlyInAnyOrder("file1.txt", "file2.txt")
                .doesNotContain("user2file.txt");

        Allure.step("Проверка содержания файлов пользователя 2");
        assertThat(user2Files)
                .extracting(File::getFileName)
                .containsExactly("user2file.txt")
                .doesNotContain("file1.txt", "file2.txt");

        Allure.step("Прямая проверка через репозиторий");
        assertThat(fileRepository.findByUser(testUser))
                .hasSize(2)
                .allMatch(file -> file.getUser().getId().equals(testUser.getId()));

        Users finalTestUser = testUser2;
        assertThat(fileRepository.findByUser(testUser2))
                .hasSize(1)
                .allMatch(file -> file.getUser().getId().equals(finalTestUser.getId()));

        Allure.step("Проверка физического расположения файлов");
        for (File file : user1Files) {
            assertThat(file.getFilePath())
                    .contains(testUser.getUsername())
                    .doesNotContain(testUser2.getUsername());
            assertThat(Files.exists(Path.of(file.getFilePath()))).isTrue();
        }

        for (File file : user2Files) {
            assertThat(file.getFilePath())
                    .contains(testUser2.getUsername())
                    .doesNotContain(testUser.getUsername());
            assertThat(Files.exists(Path.of(file.getFilePath()))).isTrue();
        }
    }


    // FileService.deleteFile() ↔ FileRepository.findById() ↔ PostgreSQL
    // FileService.deleteFile() ↔ FileRepository.deleteById() ↔ PostgreSQL
    // FileService ↔ Файловая система (удаление физического файла)
    // Согласованность операций: удаление из БД + удаление файла
    @Test
    @Order(4)
    @DisplayName("Сценарий 4: Удаление файлов и очистка данных")
    @Epic("Управление файлами")
    @Feature("Операции удаления")
    @Story("Удаление файлов из БД и файловой системы")
    @Owner("Команда разработки CloudSprint")
    @AllureId("INTEGRATION-004")
    @Description("Тест проверяет корректность удаления файлов: удаление из базы данных и физическое удаление из файловой системы. " +
            "Валидирует согласованность операций удаления.")
    void givenUploadedFile_whenDeleteFile_thenRemovedFromDbAndFileSystem() throws Exception {
        Allure.step("Создание тестового файла и загрузка");
        // ДАНО: Загруженный файл
        MockMultipartFile file = createTestFile("to_delete.txt", "Delete me");
        File uploadedFile = fileService.addFile(file, testUser).get(5, TimeUnit.SECONDS);

        Path filePath = Path.of(uploadedFile.getFilePath());
        assertThat(Files.exists(filePath)).isTrue(); // Файл существует

        Allure.step("Мокирование аутентификации");
        mockAuthentication(testUser);

        // КОГДА: Удаляем файл
        // Проверка интеграции операций удаления
        Allure.step("Удаление файла через FileService");
        fileService.deleteFile(uploadedFile.getId()).get(5, TimeUnit.SECONDS);

        Allure.step("Проверка удаления из файловой системы");
        // ТОГДА: Файл удален из БД
        // Валидация согласованности данных
        Optional<File> deletedFile = fileRepository.findById(uploadedFile.getId());
        assertThat(deletedFile).isEmpty();

        // И: Файл удален из файловой системы
        assertThat(Files.exists(filePath)).isFalse();
    }

    @Test
    @Order(5)
    @DisplayName("Сценарий 5: Изоляция данных пользователей")
    @Epic("Безопасность")
    @Feature("Изоляция данных")
    @Story("Разделение файлов между пользователями")
    @Owner("Команда разработки CloudSprint")
    @AllureId("INTEGRATION-005")
    @Description("Тест проверяет корректную изоляцию данных между разными пользователями системы. " +
            "Проверяет, что пользователи имеют доступ только к своим файлам и не могут видеть или получить(!) файлы других пользователей.")
    void givenMultipleUsers_whenUploadFiles_thenDataProperlyIsolated() throws Exception {
        Allure.step("Создание двух разных пользователей");
        // ДАНО: Два разных пользователя
        Users user1 = createUniqueUser();
        Users user2 = createUniqueUser();

        Allure.step("Создание тестовых файлов для пользователей");
        MockMultipartFile fileForUser1 = createTestFile("user1_file.txt", "User1 content");
        MockMultipartFile fileForUser2 = createTestFile("user2_file.txt", "User2 content");

        Allure.step("Загрузка файлов для пользователей");
        // КОГДА: Каждый пользователь загружает свой файл
        File uploadedFile1 = fileService.addFile(fileForUser1, user1).get(5, TimeUnit.SECONDS);
        File uploadedFile2 = fileService.addFile(fileForUser2, user2).get(5, TimeUnit.SECONDS);

        Allure.step("Проверка изоляции данных: количество файлов");
        // ТОГДА: Файлы изолированы по пользователям
        List<File> user1Files = fileService.getFilesByUser(user1);
        List<File> user2Files = fileService.getFilesByUser(user2);

        assertThat(user1Files).hasSize(1);
        assertThat(user2Files).hasSize(1);

        // === МОКИРУЕМ SecurityContext ДЛЯ ТЕСТИРОВАНИЯ БЕЗОПАСНОСТИ ===

        Allure.step("Проверка легитимного доступа пользователя 1 к своему файлу");
        // 1. Тестируем легитимный доступ (User1 к своему файлу)
        mockAuthentication(user1);
        File legitimateFile1 = fileService.getFileById(uploadedFile1.getId());
        assertThat(legitimateFile1.getId()).isEqualTo(uploadedFile1.getId());

        Allure.step("Проверка легитимного доступа пользователя 2 к своему файлу");
        // 2. Тестируем легитимный доступ (User2 к своему файлу)
        mockAuthentication(user2);
        File legitimateFile2 = fileService.getFileById(uploadedFile2.getId());
        assertThat(legitimateFile2.getId()).isEqualTo(uploadedFile2.getId());

        Allure.step("Проверка несанкционированного доступа пользователя 2 к файлу пользователя 1");
        // 3. Тестируем НЕСАНКЦИОНИРОВАННЫЙ доступ (User2 к файлу User1)
        mockAuthentication(user2);
        assertThatThrownBy(() -> {
            fileService.getFileById(uploadedFile1.getId());
        }).isInstanceOf(SecurityException.class)
                .hasMessageContaining("Access denied");

        Allure.step("Проверка несанкционированного доступа пользователя 1 к файлу пользователя 2");
        // 4. Тестируем НЕСАНКЦИОНИРОВАННЫЙ доступ (User1 к файлу User2)
        mockAuthentication(user1);
        assertThatThrownBy(() -> {
            fileService.getFileById(uploadedFile2.getId());
        }).isInstanceOf(SecurityException.class)
                .hasMessageContaining("Access denied");

        Allure.step("Проверка несанкционированного удаления файла пользователя 1 пользователем 2");
        // 5. Тестируем удаление с проверкой прав (ИСПРАВЛЕННАЯ ПРОВЕРКА)
        mockAuthentication(user2);
        assertThatThrownBy(() -> {
            fileService.deleteFile(uploadedFile1.getId()).get(5, TimeUnit.SECONDS);
        }).isInstanceOf(SecurityException.class) // SecurityException выбрасывается напрямую
                .hasMessageContaining("Access denied");

        Allure.step("Проверка легитимного удаления файла");
        // 6. Тестируем легитимное удаление
        mockAuthentication(user1);
        assertThatNoException().isThrownBy(() -> {
            fileService.deleteFile(uploadedFile1.getId()).get(5, TimeUnit.SECONDS);
        });

        Allure.step("Проверка удаления файла");
        // Проверяем, что файл действительно удален
        mockAuthentication(user1);
        assertThatThrownBy(() -> {
            fileService.getFileById(uploadedFile1.getId()); // Файл больше не существует
        }).isInstanceOf(RuntimeException.class)
                .hasMessageContaining("File not found");

        Allure.step("Проверка стандартных изоляционных свойств");
        // Стандартные проверки изоляции
        assertThat(user1Files.get(0).getUser().getId()).isEqualTo(user1.getId());
        assertThat(user2Files.get(0).getUser().getId()).isEqualTo(user2.getId());
        assertThat(uploadedFile1.getFilePath()).contains(user1.getUsername());
        assertThat(uploadedFile2.getFilePath()).contains(user2.getUsername());
    }

    // Вспомогательный метод для мокирования аутентификации
    private void mockAuthentication(Users user) {
        Authentication authentication = mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(user);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    @Order(6)
    @DisplayName("Сценарий 6: Полный цикл регистрации и аутентификации пользователя")
    @Epic("Безопасность")
    @Feature("Аутентификация")
    @Story("Регистрация и вход пользователя")
    @Owner("Команда разработки CloudSprint")
    @AllureId("INTEGRATION-06")
    @Description("Тест проверяет полный жизненный цикл пользователя: регистрация, аутентификация, доступ к защищенным ресурсам и корректность работы SecurityContext.")
    void givenNewUser_whenRegisterAndAuthenticate_thenFullAccessGranted() throws Exception {
        Allure.step("Создание нового пользователя");
        // ДАНО: Новый пользователь
        String username = "fullcycle_user_" + System.currentTimeMillis();
        String password = "securePassword123";

        Users newUser = new Users();
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.setFirstName("Полный");
        newUser.setLastName("Цикл");

        Allure.step("Регистрация пользователя через UserService");
        // КОГДА: Регистрируем пользователя через UserService
        Users registeredUser = userService.createUser(newUser);

        Allure.step("Проверка создания пользователя в БД");
        // ТОГДА: Пользователь создан в БД
        assertThat(registeredUser.getId()).isNotNull();
        assertThat(registeredUser.getUsername()).isEqualTo(username);
        assertThat(registeredUser.getBaseFolderPath()).contains(username);

        Allure.step("Проверка создания папки пользователя в файловой системе");
        // И: Папка пользователя создана в файловой системе
        Path userFolder = Path.of(registeredUser.getBaseFolderPath());
        assertThat(Files.exists(userFolder)).isTrue();
        assertThat(Files.isDirectory(userFolder)).isTrue();

        Allure.step("Проверка наличия пользователя в репозитории");
        // И: Пользователь может быть найден через репозиторий
        Optional<Users> userFromDb = userRepository.findByUsername(username);
        assertThat(userFromDb).isPresent();
        assertThat(userFromDb.get().getId()).isEqualTo(registeredUser.getId());

        Allure.step("Мокирование аутентификации");
        // КОГДА: Мокируем аутентификацию (эмулируем успешный вход)
        mockAuthentication(registeredUser);

        Allure.step("Проверка SecurityContext");
        // ТОГДА: SecurityContext содержит корректного пользователя
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertThat(authentication).isNotNull();
        assertThat(authentication.isAuthenticated()).isTrue();
        assertThat(authentication.getPrincipal()).isInstanceOf(Users.class);

        Users authenticatedUser = (Users) authentication.getPrincipal();
        assertThat(authenticatedUser.getId()).isEqualTo(registeredUser.getId());
        assertThat(authenticatedUser.getUsername()).isEqualTo(username);

        Allure.step("Загрузка тестового файла");
        // И: Пользователь может выполнять защищенные операции
        MockMultipartFile testFile = createTestFile("auth_test.txt", "Authentication test content");
        File uploadedFile = fileService.addFile(testFile, registeredUser).get(5, TimeUnit.SECONDS);

        Allure.step("Проверка принадлежности файла пользователю");
        // Проверяем, что файл действительно принадлежит пользователю
        assertThat(uploadedFile.getUser().getId()).isEqualTo(registeredUser.getId());

        Allure.step("Получение списка файлов пользователя");
        // И: Пользователь может получить список своих файлов
        List<File> userFiles = fileService.getFilesByUser(registeredUser);
        assertThat(userFiles).hasSize(1);
        assertThat(userFiles.get(0).getId()).isEqualTo(uploadedFile.getId());

        Allure.step("Скачивание файла");
        // И: Пользователь может скачать свой файл
        File downloadedFile = fileService.getFileById(uploadedFile.getId());
        assertThat(downloadedFile).isNotNull();
        assertThat(downloadedFile.getFileName()).isEqualTo("auth_test.txt");

        Allure.step("Проверка существования и доступности файла");
        // И: Физический файл существует и доступен
        Path physicalFilePath = Path.of(downloadedFile.getFilePath());
        assertThat(Files.exists(physicalFilePath)).isTrue();
        assertThat(Files.isReadable(physicalFilePath)).isTrue();
    }

    @Test
    @Order(7)
    @DisplayName("Сценарий 7: Каскадное удаление пользовательских данных")
    @Epic("Управление данными")
    @Feature("Каскадные операции")
    @Story("Удаление пользователя с файлами")
    @Owner("Команда разработки CloudSprint")
    @AllureId("INTEGRATION-007")
    @Description("Тест проверяет корректность каскадного удаления всех данных пользователя при удалении его аккаунта. " +
            "Включает удаление файлов из БД и файловой системы, обеспечивая полную очистку данных.")
    void givenUserWithFiles_whenUserDeletedViaService_thenAllFilesRemoved() throws Exception {
        Allure.step("Создание файлов для пользователя");
        // ДАНО: Пользователь с файлами
        MockMultipartFile file1 = createTestFile("cascade1.txt", "Content 1");
        MockMultipartFile file2 = createTestFile("cascade2.txt", "Content 2");

        File uploaded1 = fileService.addFile(file1, testUser).get(5, TimeUnit.SECONDS);
        File uploaded2 = fileService.addFile(file2, testUser).get(5, TimeUnit.SECONDS);

        Path path1 = Path.of(uploaded1.getFilePath());
        Path path2 = Path.of(uploaded2.getFilePath());

        Allure.step("Мокирование аутентификации");
        // ВАЖНО: Мокируем аутентификацию ДО вызова deleteUser
        mockAuthentication(testUser);

        Allure.step("Удаление пользователя через UserService");
        // КОГДА: Удаляем пользователя через UserService (бизнес-логика)
        userService.deleteUser(testUser.getId());

        Allure.step("Проверка удаления пользователя из БД");
        // ТОГДА: Проверяем интеграцию всех компонентов
        assertThat(userRepository.findById(testUser.getId())).isEmpty(); // User удален из БД

        Allure.step("Проверка удаления файлов из БД");
        assertThat(fileRepository.findByUser(testUser)).isEmpty(); // Files удалены из БД

        Allure.step("Проверка удаления физических файлов");
        assertThat(Files.exists(path1)).isFalse(); // Физические файлы удалены
        assertThat(Files.exists(path2)).isFalse();
    }

    @Test
    @Order(8)
    @DisplayName("Сценарий 8: Согласованность размеров файлов")
    @Epic("Целостность данных")
    @Feature("Валидация данных")
    @Story("Согласованность метаданных и физических файлов")
    @Owner("Команда разработки CloudSprint")
    @AllureId("INTEGRATION-008")
    @Description("Тест проверяет согласованность размеров файлов между различными уровнями системы: " +
            "базой данных, файловой системой и бизнес-логикой. Валидирует целостность метаданных.")
    void givenUploadedFile_whenCheckFileSize_thenConsistentAcrossAllLayers() throws Exception {
        Allure.step("Создание файлов разного размера");
        // ДАНО: Файлы разного размера
        String smallContent = "Small file content";
        String mediumContent = "Medium file content ".repeat(100); // ~2KB
        byte[] largeContent = new byte[1024 * 50]; // 50KB
        new java.util.Random().nextBytes(largeContent);

        MockMultipartFile smallFile = createTestFile("small.txt", smallContent);
        MockMultipartFile mediumFile = createTestFile("medium.txt", mediumContent);
        MockMultipartFile largeFile = new MockMultipartFile(
                "file", "large.bin", "application/octet-stream", largeContent
        );

        Allure.step("Загрузка файлов разных размеров через FileService");
        // КОГДА: Загружаем файлы разных размеров
        File uploadedSmall = fileService.addFile(smallFile, testUser).get(5, TimeUnit.SECONDS);
        File uploadedMedium = fileService.addFile(mediumFile, testUser).get(5, TimeUnit.SECONDS);
        File uploadedLarge = fileService.addFile(largeFile, testUser).get(5, TimeUnit.SECONDS);

        Allure.step("Мокирование аутентификации");
        mockAuthentication(testUser);
        // ТОГДА: Проверяем согласованность размеров на всех четырех уровнях
        Allure.step("Комплексная проверка согласованности размеров файлов", () -> {
            Allure.step("Проверка маленького файла (" + smallContent.getBytes().length + " байт)");
            verifyFileSizeConsistencyOnAllLayers(uploadedSmall, smallContent.getBytes().length);

            Allure.step("Проверка среднего файла (" + mediumContent.getBytes().length + " байт)");
            verifyFileSizeConsistencyOnAllLayers(uploadedMedium, mediumContent.getBytes().length);

            Allure.step("Проверка большого файла (" + largeContent.length + " байт)");
            verifyFileSizeConsistencyOnAllLayers(uploadedLarge, largeContent.length);
        });
    }

@Step("Комплексная проверка согласованности размера файла '{file.fileName}' на 4 уровнях системы")
private void verifyFileSizeConsistencyOnAllLayers(File file, long expectedSize) throws Exception {
    // Уровень 1: Бизнес-логика (объект File, возвращенный сервисом)
    Allure.step("Уровень 1 - Бизнес-логика: проверка размера в объекте File", () -> {
        assertThat(file.getFileSize())
                .as("Размер в объекте File должен соответствовать ожидаемому")
                .isEqualTo(expectedSize);
    });

    // Уровень 2: Файловая система (физическое хранение)
    Allure.step("Уровень 2 - Файловая система: проверка физического файла", () -> {
        Path filePath = Path.of(file.getFilePath());
        assertThat(Files.exists(filePath))
                .as("Физический файл должен существовать по пути: " + filePath)
                .isTrue();

        long physicalFileSize = Files.size(filePath);
        assertThat(physicalFileSize)
                .as("Размер физического файла должен соответствовать ожидаемому")
                .isEqualTo(expectedSize);
    });

    // Уровень 3: Persistence слой (база данных через репозиторий)
    Allure.step("Уровень 3 - База данных: проверка через репозиторий", () -> {
        File fromDb = fileRepository.findById(file.getId())
                .orElseThrow(() -> new RuntimeException("File not found in DB"));

        assertThat(fromDb.getFileSize())
                .as("Размер в базе данных должен соответствовать ожидаемому")
                .isEqualTo(expectedSize);
    });

    // Уровень 4: Сервисный слой (бизнес-логика через публичный API)
    Allure.step("Уровень 4 - Сервисный слой: проверка через FileService", () -> {
        File fromService = fileService.getFileById(file.getId());

        assertThat(fromService.getFileSize())
                .as("Размер, возвращаемый сервисом, должен соответствовать ожидаемому")
                .isEqualTo(expectedSize);
    });

    // Дополнительная проверка: все уровни возвращают одинаковый размер
    Allure.step("Сравнение размеров между всеми уровнями системы", () -> {
        long sizeInObject = file.getFileSize();
        long sizeInFileSystem = Files.size(Path.of(file.getFilePath()));
        long sizeInDatabase = fileRepository.findById(file.getId()).get().getFileSize();
        long sizeFromService = fileService.getFileById(file.getId()).getFileSize();

        assertThat(sizeInObject)
                .as("Все уровни должны возвращать одинаковый размер файла")
                .isEqualTo(sizeInFileSystem)
                .isEqualTo(sizeInDatabase)
                .isEqualTo(sizeFromService);
    });}


    //Presentation Layer (Controllers)
    //      ↓
    // Service Layer (Business Logic)
    //   ↓
    // Repository Layer (Data Access)
    //  ↓
    //Database + File System
    @Test
    @Order(9)
    @DisplayName("Сценарий 9: Согласованность данных между сервисным слоем и слоем доступа к данным")
    @Epic("Архитектура")
    @Feature("Слоистая архитектура")
    @Story("Проверка изоляции слоев приложения")
    @Owner("Команда разработки CloudSprint")
    @AllureId("INTEGRATION-009")
    @Description("Тест проверяет согласованность данных между сервисным слоем и слоем доступа к данным. " +
            "Валидирует, что бизнес-логика (FileService) и persistence слой (База данных) возвращают идентичные результаты для одинаковых операций.")
    void givenSystemArchitecture_whenOperationsPerformed_thenLayersProperlyIsolated() throws Exception {
        Allure.step("Создание нового пользователя и файла");
        // ДАНО: Нормальная работа системы
        Users user = createUniqueUser();
        MockMultipartFile file = createTestFile("arch_test.txt", "Architecture validation content");

        Allure.step("Загрузка файла через FileService");
        // КОГДА: Выполняем полный цикл операций через разные слои архитектуры
        File uploadedFile = fileService.addFile(file, user).get(5, TimeUnit.SECONDS);

        Allure.step("Мокирование аутентификации");
        mockAuthentication(user);

        Allure.step("Получение списка файлов через сервис и репозиторий");
        // ПРОВЕРКА: Сервисный слой не зависит от деталей реализации репозитория
        // FileService должен работать через интерфейс FileRepository, а не прямую реализацию
        List<File> filesViaService = fileService.getFilesByUser(user);
        List<File> filesViaRepository = fileRepository.findByUser(user);

        // Сравнение по ID вместо equals()
        assertThat(filesViaService).hasSameSizeAs(filesViaRepository);
        assertThat(filesViaService)
                .extracting(File::getId)
                .containsExactlyInAnyOrderElementsOf(
                        filesViaRepository.stream().map(File::getId).collect(Collectors.toList())
                );

        Allure.step("Получение файла через сервис и репозиторий");
        // ПРОВЕРКА: Изоляция бизнес-логики от инфраструктурных деталей
        File fileFromService = fileService.getFileById(uploadedFile.getId());
        File fileFromRepository = fileRepository.findById(uploadedFile.getId()).orElseThrow();

        // Бизнес-логика должна быть одинаковой независимо от способа получения данных
        assertThat(fileFromService.getFileName()).isEqualTo(fileFromRepository.getFileName());
        assertThat(fileFromService.getFileSize()).isEqualTo(fileFromRepository.getFileSize());
        assertThat(fileFromService.getContentType()).isEqualTo(fileFromRepository.getContentType());
        assertThat(fileFromService.getUser().getId()).isEqualTo(fileFromRepository.getUser().getId());
    }

    @Test
    @Order(10)
    @DisplayName("Сценарий 10: Обработка запроса несуществующего файла")
    @Epic("Обработка ошибок")
    @Feature("Управление файлами")
    @Story("Обработка исключений при работе с файлами")
    @Owner("Команда разработки CloudSprint")
    @AllureId("INTEGRATION-010")
    @Description("Тест проверяет корректность обработки запросов к несуществующим файлам на всех уровнях приложения: контроллер, сервис, репозиторий.")
    void givenAuthenticatedUser_whenAccessNonExistentFile_thenProperErrorHandling() throws Exception {
        Allure.step("Создание аутентифицированного пользователя");
        // ДАНО: Аутентифицированный пользователь
        Users user = createUniqueUser();
        mockAuthentication(user);

        Allure.step("Попытка доступа к несуществующему файлу");
        // КОГДА: Пытаемся получить несуществующий файл
        Long nonExistentFileId = 999999L;

        Allure.step("Проверка репозитория на пустой результат");
        // ТОГДА: Репозиторий должен возвращать пустой Optional
        Optional<File> fromRepo = fileRepository.findById(nonExistentFileId);
        assertThat(fromRepo).as("Репозиторий не должен возвращать файл с несуществующим ID").isEmpty();

        Allure.step("Проверка сервиса на выброс исключения при получении файла");
        // И: Сервис должен выбрасывать RuntimeException при попытке получить файл
        assertThatThrownBy(() -> fileService.getFileById(nonExistentFileId))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("File not found");

        Allure.step("Проверка сервиса на выброс исключения при удалении несуществующего файла");
        // И: Удаление несуществующего файла также должно вызывать ошибку
        // ПОЧЕМУ? Потому что deleteFile вызывает getFileById внутри — и он синхронно падает!
        assertThatThrownBy(() -> fileService.deleteFile(nonExistentFileId).get(5, TimeUnit.SECONDS))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("File not found");

        Allure.step("Проверка пустого списка файлов пользователя");
        // И: Список файлов пользователя пуст
        List<File> userFiles = fileService.getFilesByUser(user);
        assertThat(userFiles).isEmpty();

        Allure.step("Прямая проверка пустого списка файлов через репозиторий");
        // И: Прямой запрос к репозиторию также возвращает пустой список
        List<File> filesFromRepo = fileRepository.findByUser(user);
        assertThat(filesFromRepo).isEmpty();

        Allure.step("Проверка существования пользователя и его папки");
        // ДОПОЛНИТЕЛЬНО: Проверяем, что пользователь и папка существуют
        assertThat(userRepository.findById(user.getId())).isPresent();
        assertThat(Files.exists(Path.of(user.getBaseFolderPath()))).isTrue();
    }

    @Test
    @Order(11)
    @DisplayName("Сценарий 11: Аутентификация с неверным паролем")
    @Epic("Аутентификация пользователей")
    @Feature("Интеграционные тесты безопасности")
    @Story("Обработка неверных учетных данных")
    @Owner("Команда разработки CloudSprint")
    @AllureId("INTEGRATION-012")
    @Description("Интеграционный тест проверяет обработку неверного пароля на всех слоях приложения: контроллер, сервис, репозиторий, Spring Security")
    @Severity(SeverityLevel.CRITICAL)
    public void test11_LoginWithInvalidPassword_ShouldReturnError() throws Exception {

        Allure.step("Проверка отсутствия пользователя в БД");
        // === 1. ПОДГОТОВКА: Проверяем, что пользователя нет в БД ===
        assertFalse(userRepository.findByUsername("testuser11").isPresent(),
                "Пользователь не должен существовать перед тестом");

        Allure.step("Создание пользователя в БД");
        // === 2. ПРОВЕРКА СЛОЯ РЕПОЗИТОРИЯ: Сохраняем пользователя в БД ===
        Users testUser = new Users();
        testUser.setUsername("testuser11");
        String correctPasswordHash = passwordEncoder.encode("correctPassword123");
        testUser.setPassword(correctPasswordHash);
        testUser.setFirstName("Test");
        testUser.setLastName("User");
        testUser.setBaseFolderPath("/uploads/testuser11");

        Users savedUser = userRepository.save(testUser);
        assertNotNull(savedUser.getId(), "Пользователь должен быть сохранен в БД с ID");

        Allure.step("Проверка записи о пользователе в БД");
        // Явная проверка записи в БД
        Users userFromDb = userRepository.findByUsername("testuser11")
                .orElseThrow(() -> new AssertionError("Пользователь должен существовать в БД"));
        assertEquals("testuser11", userFromDb.getUsername(), "Данные должны совпадать с сохраненными");

        Allure.step("Проверка сервиса аутентификации UserDetailsServiceImpl");
        // === 3. ПРОВЕРКА СЛОЯ СЕРВИСА (UserDetailsServiceImpl) ===
        UserDetails userDetails = userDetailsService.loadUserByUsername("testuser11");
        assertNotNull(userDetails, "UserDetailsService должен вернуть пользователя");
        assertEquals("testuser11", userDetails.getUsername(), "Username должен совпадать");

        // Проверка, что пароль из БД корректно хэширован
        assertTrue(passwordEncoder.matches("correctPassword123", userDetails.getPassword()),
                "Пароль должен корректно проверяться через PasswordEncoder");

        Allure.step("Проверка аутентификации с неверным паролем через контроллер и SPRING SECURITY. Добавляем CSRF токен для запроса");
        // === 4. ПРОВЕРКА ВЗАИМОДЕЙСТВИЯ СЛОЕВ ЧЕРЕЗ SPRING SECURITY ===
        // Добавляем CSRF токен для запроса
        mockMvc.perform(post("/login")
                        .param("username", "testuser11")
                        .param("password", "wrongPassword") // Неверный пароль
                        .with(csrf()) // Важно: добавляем CSRF токен
                        .contentType("application/x-www-form-urlencoded"))
                .andExpect(status().is3xxRedirection()) // Теперь должен быть редирект
                .andExpect(redirectedUrl("/login?error"));

        Allure.step("Проверка доступа к защищенному ресурсу, redirectedUrlPattern(\"**/login\")");
        // === 5. ПРОВЕРКА, ЧТО ПОЛЬЗОВАТЕЛЬ НЕ АУТЕНТИФИЦИРОВАН ===
        mockMvc.perform(get("/home"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

}
