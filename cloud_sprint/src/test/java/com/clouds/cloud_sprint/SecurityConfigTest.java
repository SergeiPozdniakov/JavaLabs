package com.clouds.cloud_sprint;

import com.clouds.cloud_sprint.security.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.junit.jupiter.api.Assertions.*;
import io.qameta.allure.*;

@Epic("Безопасность")
@Feature("Конфигурация безопасности")
@Story("Тестирование конфигурации безопасности")
@Owner("Команда разработки CloudSprint")
@Severity(SeverityLevel.CRITICAL)
class SecurityConfigTest {

    private SecurityConfig securityConfig = new SecurityConfig();

    @Test
    @Description("Тест проверяет создание кодировщика паролей. " +
            "Используется техника тест-дизайна 'Классы эквивалентности' для проверки работы системы " +
            "с корректными данными. Тест важен для обеспечения корректного создания кодировщика паролей.")
    @AllureId("SEC-CONFIG-001")
    @Step("Проверка создания кодировщика паролей")
    void testPasswordEncoder() {
        Allure.step("Создание кодировщика паролей");
        // Создание кодировщика паролей
        PasswordEncoder encoder = securityConfig.passwordEncoder();

        Allure.step("Проверка создания кодировщика паролей");
        assertNotNull(encoder);
        assertTrue(encoder instanceof BCryptPasswordEncoder);
    }

    @Test
    @Description("Тест проверяет кодирование пароля. " +
            "Используется техника тест-дизайна 'Классы эквивалентности' для проверки работы системы " +
            "с корректными данными. Тест важен для обеспечения корректного кодирования паролей.")
    @AllureId("SEC-CONFIG-002")
    @Step("Проверка кодирования пароля")
    void testPasswordEncoding() {
        Allure.step("Создание кодировщика паролей");
        // Кодирование пароля
        PasswordEncoder encoder = securityConfig.passwordEncoder();
        String rawPassword = "password";
        String encodedPassword = encoder.encode(rawPassword);

        Allure.step("Проверка закодированного пароля");
        assertNotNull(encodedPassword);
        assertNotEquals(rawPassword, encodedPassword);
        assertTrue(encoder.matches(rawPassword, encodedPassword));
    }
}
