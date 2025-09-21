package com.clouds.cloud_sprint;

import com.clouds.cloud_sprint.model.Users;
import io.qameta.allure.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import java.util.Collection;
import static org.junit.jupiter.api.Assertions.*;

@Epic("Аутентификация")
@Feature("Модель пользователя")
@Story("Тестирование модели пользователя")
@Owner("Команда разработки CloudSprint")
@Severity(SeverityLevel.NORMAL)
class UsersTest {

    private Users user;

    @BeforeEach
    void setUp() {
        user = new Users();
    }

    @Test
    @Description("Тест проверяет установку и получение ID пользователя. " +
            "Используется техника тест-дизайна 'Классы эквивалентности' для проверки работы системы " +
            "с различными ID пользователей. Тест важен для обеспечения корректной работы с ID " +
            "пользователей.")
    @AllureId("AUTH-MODEL-001")
    @Step("Проверка установки и получения ID пользователя")
    void testUserId() {
        Allure.step("Установка ID пользователя");
        // Установка и получение ID пользователя
        user.setId(1L);

        Allure.step("Проверка ID пользователя");
        assertEquals(1L, user.getId());
    }

    @Test
    @Description("Тест проверяет установку и получение имени пользователя. " +
            "Используется техника тест-дизайна 'Классы эквивалентности' для проверки работы системы " +
            "с различными именами пользователей. Тест важен для обеспечения корректной работы с " +
            "именами пользователей.")
    @AllureId("AUTH-MODEL-002")
    @Step("Проверка установки и получения имени пользователя")
    void testUsername() {
        Allure.step("Установка имени пользователя");
        // Установка и получение имени пользователя
        user.setUsername("testuser");

        Allure.step("Проверка имени пользователя");
        assertEquals("testuser", user.getUsername());
    }

    @Test
    @Description("Тест проверяет установку и получение пароля пользователя. " +
            "Используется техника тест-дизайна 'Классы эквивалентности' для проверки работы системы " +
            "с различными паролями. Тест важен для обеспечения корректной работы с паролями " +
            "пользователей.")
    @AllureId("AUTH-MODEL-003")
    @Step("Проверка установки и получения пароля пользователя")
    void testPassword() {
        Allure.step("Установка пароля пользователя");
        // Установка и получение пароля
        user.setPassword("password");

        Allure.step("Проверка пароля пользователя");
        assertEquals("password", user.getPassword());
    }

    @Test
    @Description("Тест проверяет установку и получение имени пользователя. " +
            "Используется техника тест-дизайна 'Классы эквивалентности' для проверки работы системы " +
            "с различными именами. Тест важен для обеспечения корректной работы с именами пользователей.")
    @AllureId("AUTH-MODEL-004")
    @Step("Проверка установки и получения имени пользователя")
    void testFirstName() {
        Allure.step("Установка имени");
        // Установка и получение имени
        user.setFirstName("John");

        Allure.step("Проверка имени");
        assertEquals("John", user.getFirstName());
    }

    @Test
    @Description("Тест проверяет установку и получение фамилии пользователя. " +
            "Используется техника тест-дизайна 'Классы эквивалентности' для проверки работы системы " +
            "с различными фамилиями. Тест важен для обеспечения корректной работы с фамилиями " +
            "пользователей.")
    @AllureId("AUTH-MODEL-005")
    @Step("Проверка установки и получения фамилии пользователя")
    void testLastName() {
        Allure.step("Установка фамилии");
        // Установка и получение фамилии
        user.setLastName("Doe");

        Allure.step("Проверка фамилии");
        assertEquals("Doe", user.getLastName());
    }

    @Test
    @Description("Тест проверяет установку и получение базового пути папки пользователя. " +
            "Используется техника тест-дизайна 'Классы эквивалентности' для проверки работы системы " +
            "с различными путями папок. Тест важен для обеспечения корректной работы с путями папок " +
            "пользователей.")
    @AllureId("AUTH-MODEL-006")
    @Step("Проверка установки и получения базового пути папки пользователя")
    void testBaseFolderPath() {
        Allure.step("Установка базового пути папки");
        // Установка и получение базового пути папки
        user.setBaseFolderPath("/home/user");

        Allure.step("Проверка базового пути папки");
        assertEquals("/home/user", user.getBaseFolderPath());
    }

    @Test
    @Description("Тест проверяет получение authorities пользователя. " +
            "Используется техника тест-дизайна 'Классы эквивалентности' для проверки работы системы " +
            "с authorities пользователя. Тест важен для обеспечения корректной работы с authorities " +
            "пользователей.")
    @AllureId("AUTH-MODEL-007")
    @Step("Проверка получения authorities пользователя")
    void testAuthorities() {
        Allure.step("Получение authorities пользователя");
        // Получение authorities пользователя
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

        Allure.step("Проверка authorities");
        assertNotNull(authorities);
        assertEquals(1, authorities.size());
    }

    @Test
    @Description("Тест проверяет срока действия аккаунта. " +
            "Используется техника тест-дизайна 'Классы эквивалентности' для проверки работы системы " +
            "с неистекшими аккаунтами. Тест важен для обеспечения корректной работы с сроком действия " +
            "аккаунта.")
    @AllureId("AUTH-MODEL-008")
    @Step("Проверка срока действия аккаунта")
    void testAccountNonExpired() {
        Allure.step("Проверка срока действия аккаунта");
        // Проверка срока действия аккаунта
        assertTrue(user.isAccountNonExpired());
    }

    @Test
    @Description("Тест проверяет блокировку аккаунта. " +
            "Используется техника тест-дизайна 'Классы эквивалентности' для проверки работы системы " +
            "с разблокированными аккаунтами. Тест важен для обеспечения корректной работы с " +
            "блокировкой аккаунта.")
    @AllureId("AUTH-MODEL-009")
    @Step("Проверка блокировки аккаунта")
    void testAccountNonLocked() {
        Allure.step("Проверка блокировки аккаунта");
        // Проверка блокировки аккаунта
        assertTrue(user.isAccountNonLocked());
    }

    @Test
    @Description("Тест проверяет срока действия учетных данных. " +
            "Используется техника тест-дизайна 'Классы эквивалентности' для проверки работы системы " +
            "с неистекшими учетными данными. Тест важен для обеспечения корректной работы с " +
            "сроком действия учетных данных.")
    @AllureId("AUTH-MODEL-010")
    @Step("Проверка срока действия учетных данных")
    void testCredentialsNonExpired() {
        Allure.step("Проверка срока действия учетных данных");
        // Проверка срока действия учетных данных
        assertTrue(user.isCredentialsNonExpired());
    }

    @Test
    @Description("Тест проверяет активность аккаунта. " +
            "Используется техника тест-дизайна 'Классы эквивалентности' для проверки работы системы " +
            "с активными аккаунтами. Тест важен для обеспечения корректной работы с активностью " +
            "аккаунта.")
    @AllureId("AUTH-MODEL-011")
    @Step("Проверка активности аккаунта")
    void testEnabled() {
        Allure.step("Проверка активности аккаунта");
        // Проверка активности аккаунта
        assertTrue(user.isEnabled());
    }
}
