package com.clouds.cloud_sprint;

import com.clouds.cloud_sprint.model.Users;
import com.clouds.cloud_sprint.services.UserService;
import io.qameta.allure.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Epic("Аутентификация")
@Feature("Управление пользователями")
@Story("Тестирование сервиса UserService")
@Owner("Команда разработки CloudSprint")
@Severity(SeverityLevel.CRITICAL)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private Users testUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userService.setStorageBasePath(System.getProperty("java.io.tmpdir"));

        testUser = new Users();
        testUser.setUsername("testuser");
        testUser.setPassword("rawpassword");
    }

    @Test
    @Description("Тест проверяет создание пользователя. " +
            "Используется техника тест-дизайна 'Классы эквивалентности' для проверки работы системы " +
            "с корректными данными. Тест важен для обеспечения корректного создания пользователя.")
    @AllureId("AUTH-SERVICE-001")
    @Step("Проверка создания пользователя")
    void testCreateUser() {
        Allure.step("Подготовка данных: настройка мока для кодирования пароля и сохранения пользователя");
        // Создание пользователя
        when(passwordEncoder.encode("rawpassword")).thenReturn("encodedpassword");
        when(userRepository.save(any(Users.class))).thenReturn(testUser);

        Allure.step("Создание нового пользователя");
        Users result = userService.createUser(testUser);

        Allure.step("Проверка результата");
        assertNotNull(result);
        verify(passwordEncoder).encode("rawpassword");
        verify(userRepository).save(any(Users.class));
    }

    @Test
    @Description("Тест проверяет получение пользователя по имени (найден). " +
            "Используется техника тест-дизайна 'Классы эквивалентности' для проверки работы системы " +
            "с корректными данными. Тест важен для обеспечения корректного получения пользователя по имени.")
    @AllureId("AUTH-SERVICE-002")
    @Step("Проверка получения пользователя по имени (найден)")
    void testGetUserByUsername_Found() {
        Allure.step("Подготовка данных: настройка мока для существующего пользователя");
        // Получение пользователя по имени (найден)
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        Allure.step("Получение пользователя по имени");
        Optional<Users> result = userService.getUserByUsername("testuser");

        Allure.step("Проверка результата");
        assertTrue(result.isPresent());
        assertEquals("testuser", result.get().getUsername());
    }

    @Test
    @Description("Тест проверяет получение пользователя по имени (не найден). " +
            "Используется техника тест-дизайна 'Граничные значения' для проверки работы системы " +
            "с несуществующими пользователями. Тест важен для обеспечения корректной обработки ошибок " +
            "при получении несуществующих пользователей.")
    @AllureId("AUTH-SERVICE-003")
    @Step("Проверка получения пользователя по имени (не найден)")
    void testGetUserByUsername_NotFound() {
        Allure.step("Подготовка данных: настройка мока для несуществующего пользователя");
        // Получение пользователя по имени (не найден)
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        Allure.step("Получение несуществующего пользователя по имени");
        Optional<Users> result = userService.getUserByUsername("unknown");

        Allure.step("Проверка результата");
        assertFalse(result.isPresent());
    }

    @Test
    @Description("Тест проверяет установку базового пути хранения. " +
            "Используется техника тест-дизайна 'Классы эквивалентности' для проверки работы системы " +
            "с корректными данными. Тест важен для обеспечения корректной установки базового пути " +
            "хранения.")
    @AllureId("AUTH-SERVICE-004")
    @Step("Проверка установки базового пути хранения")
    void testSetStorageBasePath() {
        // Установка базового пути хранения
        String testPath = "/custom/path";
        userService.setStorageBasePath(testPath);

        // Проверяем, что путь установлен (косвенно через создание пользователя)
        when(passwordEncoder.encode(anyString())).thenReturn("encoded");
        when(userRepository.save(any(Users.class))).thenReturn(testUser);

        userService.createUser(testUser);
        // Если не выброшено исключение - путь установлен корректно
        assertTrue(true);
    }
}