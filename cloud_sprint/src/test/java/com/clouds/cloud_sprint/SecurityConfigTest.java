package com.clouds.cloud_sprint;

import com.clouds.cloud_sprint.security.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

class SecurityConfigTest {

    private SecurityConfig securityConfig = new SecurityConfig();

    @Test
    void testPasswordEncoder() {
        // Test 35: Создание кодировщика паролей
        PasswordEncoder encoder = securityConfig.passwordEncoder();
        assertNotNull(encoder);
        assertTrue(encoder instanceof BCryptPasswordEncoder);
    }

    @Test
    void testPasswordEncoding() {
        // Test 36: Кодирование пароля
        PasswordEncoder encoder = securityConfig.passwordEncoder();
        String rawPassword = "password";
        String encodedPassword = encoder.encode(rawPassword);

        assertNotNull(encodedPassword);
        assertNotEquals(rawPassword, encodedPassword);
        assertTrue(encoder.matches(rawPassword, encodedPassword));
    }
}
