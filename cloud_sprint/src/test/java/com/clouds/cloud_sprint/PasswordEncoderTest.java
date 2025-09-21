package com.clouds.cloud_sprint;

import io.qameta.allure.*;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.junit.jupiter.api.Assertions.*;

@Epic("Безопасность")
@Feature("Кодирование паролей")
@Story("Тестирование кодирования паролей")
@Owner("Команда разработки CloudSprint")
@Severity(SeverityLevel.CRITICAL)
class PasswordEncoderTest {

    @Test
    @Description("Тест проверяет консистентность хэширования паролей. " +
            "Используется техника тест-дизайна 'Классы эквивалентности' для проверки работы системы " +
            "с корректными данными. Тест важен для обеспечения корректного хэширования паролей.")
    @AllureId("SEC-PASSWORD-001")
    @Step("Проверка консистентности хэширования паролей")
    void testPasswordHashingConsistency() {
        Allure.step("Создание кодировщика паролей");
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "mySecurePassword123";

        String hash1 = encoder.encode(rawPassword);
        String hash2 = encoder.encode(rawPassword);

        Allure.step("Проверка консистентности хэширования паролей");
        assertNotEquals(hash1, hash2);
        assertTrue(encoder.matches(rawPassword, hash1));
        assertTrue(encoder.matches(rawPassword, hash2));
    }

    @Test
    @Description("Тест проверяет верификацию паролей. " +
            "Используется техника тест-дизайна 'Классы эквивалентности' для проверки работы системы " +
            "с корректными данными. Тест важен для обеспечения корректной верификации паролей.")
    @AllureId("SEC-PASSWORD-002")
    @Step("Проверка верификации паролей")
    void testPasswordVerification() {
        Allure.step("Создание кодировщика паролей");
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        String correctPassword = "correctPassword";
        String wrongPassword = "wrongPassword";
        String encodedPassword = encoder.encode(correctPassword);

        Allure.step("Проверка верификации паролей");
        assertTrue(encoder.matches(correctPassword, encodedPassword));
        assertFalse(encoder.matches(wrongPassword, encodedPassword));
    }
}
