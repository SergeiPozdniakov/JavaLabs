package com.clouds.cloud_sprint;

import com.clouds.cloud_sprint.model.Users;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class UsersTest {

    private Users user;

    @BeforeEach
    void setUp() {
        user = new Users();
    }

    @Test
    void testUserId() {
        // Установка и получение ID пользователя
        user.setId(1L);
        assertEquals(1L, user.getId());
    }

    @Test
    void testUsername() {
        // Установка и получение имени пользователя
        user.setUsername("testuser");
        assertEquals("testuser", user.getUsername());
    }

    @Test
    void testPassword() {
        // Установка и получение пароля
        user.setPassword("password");
        assertEquals("password", user.getPassword());
    }

    @Test
    void testFirstName() {
        // Установка и получение имени
        user.setFirstName("John");
        assertEquals("John", user.getFirstName());
    }

    @Test
    void testLastName() {
        // Установка и получение фамилии
        user.setLastName("Doe");
        assertEquals("Doe", user.getLastName());
    }

    @Test
    void testBaseFolderPath() {
        // Test 29: Установка и получение базового пути папки
        user.setBaseFolderPath("/home/user");
        assertEquals("/home/user", user.getBaseFolderPath());
    }

    @Test
    void testAuthorities() {
        // Получение authorities пользователя
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        assertNotNull(authorities);
        assertEquals(1, authorities.size());
    }

    @Test
    void testAccountNonExpired() {
        // Проверка срока действия аккаунта
        assertTrue(user.isAccountNonExpired());
    }

    @Test
    void testAccountNonLocked() {
        // Проверка блокировки аккаунта
        assertTrue(user.isAccountNonLocked());
    }

    @Test
    void testCredentialsNonExpired() {
        // Проверка срока действия учетных данных
        assertTrue(user.isCredentialsNonExpired());
    }

    @Test
    void testEnabled() {
        // Проверка активности аккаунта
        assertTrue(user.isEnabled());
    }
}
