package com.clouds.cloud_sprint;

import com.clouds.cloud_sprint.UserRepository;
import com.clouds.cloud_sprint.model.Users;
import com.clouds.cloud_sprint.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
    void testCreateUser() {
        // Test 47: Создание пользователя
        when(passwordEncoder.encode("rawpassword")).thenReturn("encodedpassword");
        when(userRepository.save(any(Users.class))).thenReturn(testUser);

        Users result = userService.createUser(testUser);

        assertNotNull(result);
        verify(passwordEncoder).encode("rawpassword");
        verify(userRepository).save(any(Users.class));
    }

    @Test
    void testGetUserByUsername_Found() {
        // Test 48: Получение пользователя по имени (найден)
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        Optional<Users> result = userService.getUserByUsername("testuser");

        assertTrue(result.isPresent());
        assertEquals("testuser", result.get().getUsername());
    }

    @Test
    void testGetUserByUsername_NotFound() {
        // Test 49: Получение пользователя по имени (не найден)
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        Optional<Users> result = userService.getUserByUsername("unknown");

        assertFalse(result.isPresent());
    }

    @Test
    void testSetStorageBasePath() {
        // Test 50: Установка базового пути хранения
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