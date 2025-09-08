package com.clouds.cloud_sprint;

import com.clouds.cloud_sprint.model.Users;
import com.clouds.cloud_sprint.services.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private MockedStatic<Files> mockedFiles;
    private MockedStatic<Paths> mockedPaths;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Открываем моки для статических классов
        mockedFiles = Mockito.mockStatic(Files.class);
        mockedPaths = Mockito.mockStatic(Paths.class);

        // 👇 КРИТИЧЕСКИ ВАЖНО: Устанавливаем значение basePath ВРУЧНУЮ
        userService.setStorageBasePath("C:\\test_storage"); // Или любой другой путь
    }

    @AfterEach
    void tearDown() {
        if (mockedFiles != null) {
            mockedFiles.close();
        }
        if (mockedPaths != null) {
            mockedPaths.close();
        }
    }

    @Test
    void testCreateUser() throws Exception {
        // Arrange
        Users inputUser = new Users();
        inputUser.setUsername("testuser");
        inputUser.setPassword("password");
        inputUser.setFirstName("John");
        inputUser.setLastName("Doe");

        // Мокаем возвращаемый объект из save
        Users savedUser = new Users();
        savedUser.setId(1L);
        savedUser.setUsername("testuser");
        savedUser.setPassword("encodedPassword");
        savedUser.setFirstName("John");
        savedUser.setLastName("Doe");
        savedUser.setBaseFolderPath("C:\\test_storage\\testuser");

        // Мокаем статические вызовы
        Path mockPath = mock(Path.class);
        mockedPaths.when(() -> Paths.get("C:\\test_storage")).thenReturn(mockPath);
        mockedPaths.when(() -> mockPath.resolve("testuser")).thenReturn(mockPath);
        mockedFiles.when(() -> Files.createDirectories(mockPath)).thenReturn(null);

        // Мокаем зависимости
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(any(Users.class))).thenReturn(savedUser);

        // Act
        Users createdUser = userService.createUser(inputUser);

        // Assert
        assertNotNull(createdUser);
        assertEquals("encodedPassword", createdUser.getPassword());
        assertEquals("C:\\test_storage\\testuser", createdUser.getBaseFolderPath());
        assertEquals("testuser", createdUser.getUsername());

        // Verify
        verify(passwordEncoder, times(1)).encode("password");
        verify(userRepository, times(1)).save(any(Users.class));
        mockedPaths.verify(() -> Paths.get("C:\\test_storage"));
        mockedFiles.verify(() -> Files.createDirectories(mockPath));
    }

    @Test
    void testGetUserByUsername() {
        // Arrange
        Users user = new Users();
        user.setUsername("testuser");
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        // Act
        Optional<Users> foundUser = userService.getUserByUsername("testuser");

        // Assert
        assertTrue(foundUser.isPresent());
        assertEquals("testuser", foundUser.get().getUsername());
    }

    @Test
    void testCreateUser_FailsWhenDirectoryCreationFails() {
        // Arrange
        Users user = new Users();
        user.setUsername("testuser");
        user.setPassword("password");

        Path mockPath = mock(Path.class);
        mockedPaths.when(() -> Paths.get("C:\\test_storage")).thenReturn(mockPath);
        mockedPaths.when(() -> mockPath.resolve("testuser")).thenReturn(mockPath);
        mockedFiles.when(() -> Files.createDirectories(mockPath)).thenThrow(new IOException("Access denied"));

        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.createUser(user);
        });

        assertTrue(exception.getMessage().startsWith("Failed to create user folder"));
        verify(userRepository, never()).save(any());
    }
}