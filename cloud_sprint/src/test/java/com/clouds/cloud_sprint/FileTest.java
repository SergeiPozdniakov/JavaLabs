package com.clouds.cloud_sprint;

import com.clouds.cloud_sprint.model.File;
import com.clouds.cloud_sprint.model.Users;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import io.qameta.allure.*;

@Epic("Модели данных")
@Feature("Модель файла")
@Story("Тестирование модели File")
@Owner("Команда разработки CloudSprint")
@Severity(SeverityLevel.NORMAL)
class FileTest {

    private File file;
    private Users user;

    @BeforeEach
    void setUp() {
        file = new File();
        user = new Users();
    }

    @Test
    @Description("Тест проверяет установку и получение ID файла. " +
            "Используется техника тест-дизайна 'Классы эквивалентности' для проверки работы системы " +
            "с различными ID файлов. Тест важен для обеспечения корректной работы с ID файлов.")
    @AllureId("MODEL-FILE-001")
    @Step("Проверка установки и получения ID файла")
    void testFileId() {
        Allure.step("Установка ID файла");
        // Установка и получение ID файла
        file.setId(1L);

        Allure.step("Проверка ID файла");
        assertEquals(1L, file.getId());
    }

    @Test
    @Description("Тест проверяет установку и получение имени файла. " +
            "Используется техника тест-дизайна 'Классы эквивалентности' для проверки работы системы " +
            "с различными именами файлов. Тест важен для обеспечения корректной работы с именами файлов.")
    @AllureId("MODEL-FILE-002")
    @Step("Проверка установки и получения имени файла")
    void testFileName() {
        Allure.step("Установка имени файла");
        // Установка и получение имени файла
        file.setFileName("test.txt");

        Allure.step("Проверка имени файла");
        assertEquals("test.txt", file.getFileName());
    }

    @Test
    @Description("Тест проверяет установку и получение типа контента файла. " +
            "Используется техника тест-дизайна 'Классы эквивалентности' для проверки работы системы " +
            "с различными типами контента. Тест важен для обеспечения корректной работы с типами " +
            "контента файлов.")
    @AllureId("MODEL-FILE-003")
    @Step("Проверка установки и получения типа контента файла")
    void testContentType() {
        Allure.step("Установка типа контента файла");
        // Установка и получение типа контента
        file.setContentType("text/plain");

        Allure.step("Проверка типа контента файла");
        assertEquals("text/plain", file.getContentType());
    }

    @Test
    @Description("Тест проверяет установку и получение размера файла. " +
            "Используется техника тест-дизайна 'Классы эквивалентности' для проверки работы системы " +
            "с различными размерами файлов. Тест важен для обеспечения корректной работы с размерами " +
            "файлов.")
    @AllureId("MODEL-FILE-004")
    @Step("Проверка установки и получения размера файла")
    void testFileSize() {
        Allure.step("Установка размера файла");
        // Установка и получение размера файла
        file.setFileSize(1024L);

        Allure.step("Проверка размера файла");
        assertEquals(1024L, file.getFileSize());
    }

    @Test
    @Description("Тест проверяет установку и получение пути файла. " +
            "Используется техника тест-дизайна 'Классы эквивалентности' для проверки работы системы " +
            "с различными путями файлов. Тест важен для обеспечения корректной работы с путями файлов.")
    @AllureId("MODEL-FILE-005")
    @Step("Проверка установки и получения пути файла")
    void testFilePath() {
        Allure.step("Установка пути файла");
        // Установка и получение пути файла
        file.setFilePath("/path/to/file.txt");

        Allure.step("Проверка пути файла");
        assertEquals("/path/to/file.txt", file.getFilePath());
    }

    @Test
    @Description("Тест проверяет установку и получение пользователя файла. " +
            "Используется техника тест-дизайна 'Классы эквивалентности' для проверки работы системы " +
            "с различными пользователями. Тест важен для обеспечения корректной работы с пользователями " +
            "файлов.")
    @AllureId("MODEL-FILE-006")
    @Step("Проверка установки и получения пользователя файла")
    void testFileUser() {
        Allure.step("Установка пользователя файла");
        // Установка и получение пользователя файла
        file.setUser(user);

        Allure.step("Проверка пользователя файла");
        assertEquals(user, file.getUser());
    }
}
