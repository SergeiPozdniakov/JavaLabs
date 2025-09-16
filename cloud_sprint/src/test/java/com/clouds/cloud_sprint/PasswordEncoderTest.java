package com.clouds.cloud_sprint;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

class PasswordEncoderTest {

    @Test
    void testPasswordHashingConsistency() {

        PasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "mySecurePassword123";

        String hash1 = encoder.encode(rawPassword);
        String hash2 = encoder.encode(rawPassword);


        assertNotEquals(hash1, hash2);

        assertTrue(encoder.matches(rawPassword, hash1));
        assertTrue(encoder.matches(rawPassword, hash2));
    }

    @Test
    void testPasswordVerification() {

        PasswordEncoder encoder = new BCryptPasswordEncoder();
        String correctPassword = "correctPassword";
        String wrongPassword = "wrongPassword";

        String encodedPassword = encoder.encode(correctPassword);

        assertTrue(encoder.matches(correctPassword, encodedPassword));
        assertFalse(encoder.matches(wrongPassword, encodedPassword));
    }
}
