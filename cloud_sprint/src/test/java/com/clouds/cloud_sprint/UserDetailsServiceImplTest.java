package com.clouds.cloud_sprint;

import com.clouds.cloud_sprint.model.Users;
import com.clouds.cloud_sprint.security.UserDetailsServiceImpl;
import io.qameta.allure.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Epic("Аутентификация")
@Feature("Пользовательские данные")
@Story("Тестирование сервиса UserDetailsServiceImpl")
@Owner("Команда разработки CloudSprint")
@Severity(SeverityLevel.CRITICAL)
class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @Description("Тест проверяет загрузку пользователя по имени. " +
            "Используется техника тест-дизайна 'Классы эквивалентности' для проверки работы системы " +
            "с корректными данными. Тест важен для обеспечения корректной загрузки пользователя по имени.")
    @AllureId("AUTH-USER-001")
    @Step("Проверка загрузки пользователя по имени")
    void testLoadUserByUsername() {
        Allure.step("Подготовка данных: создание пользователя");
        Users user = new Users();
        user.setUsername("testuser");
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        Allure.step("Загрузка пользователя по имени");
        Users result = (Users) userDetailsService.loadUserByUsername("testuser");

        Allure.step("Проверка результата");
        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        verify(userRepository, times(1)).findByUsername("testuser");
    }

    @Test
    @Description("Тест проверяет исключение при загрузке несуществующего пользователя. " +
            "Используется техника тест-дизайна 'Граничные значения' для проверки работы системы " +
            "с несуществующими пользователями. Тест важен для обеспечения корректной обработки ошибок " +
            "при загрузке несуществующих пользователей.")
    @AllureId("AUTH-USER-002")
    @Step("Проверка исключения при загрузке несуществующего пользователя")
    void testLoadUserByUsernameThrowsException() {
        Allure.step("Подготовка данных: создание несуществующего пользователя");
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        Allure.step("Попытка загрузки несуществующего пользователя");
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("unknown");
        });

        Allure.step("Проверка исключения");
        assertEquals("User not found", exception.getMessage());
        verify(userRepository, times(1)).findByUsername("unknown");
    }
}
