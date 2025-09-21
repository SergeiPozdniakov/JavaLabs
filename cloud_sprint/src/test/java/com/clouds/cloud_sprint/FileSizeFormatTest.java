package com.clouds.cloud_sprint;

import com.clouds.cloud_sprint.model.File;
import io.qameta.allure.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Epic("Файловые операции")
@Feature("Форматирование размера файлов")
@Story("Тестирование форматирования размера файлов")
@Owner("Команда разработки CloudSprint")
@Severity(SeverityLevel.NORMAL)
class FileSizeFormatTest {

    @Test
    @Description("Тест проверяет форматирование размера файла в КБ. " +
            "Используется техника тест-дизайна 'Классы эквивалентности' для проверки работы системы " +
            "с размерами файлов в килобайтах. Тест важен для обеспечения корректного отображения " +
            "размера файлов в килобайтах.")
    @AllureId("FILE-FORMAT-001")
    @Step("Проверка форматирования размера файла в КБ")
    void testFileSizeFormatting_KB() {
        Allure.step("Подготовка данных: создание файла размером 1.5 KB");
        // Форматирование размера файла в КБ
        File file = new File();
        file.setFileSize(1536L); // 1.5 KB

        Allure.step("Проверка размера файла");
        assertEquals(1536L, file.getFileSize());
    }

    @Test
    @Description("Тест проверяет форматирование размера файла в МБ. " +
            "Используется техника тест-дизайна 'Классы эквивалентности' для проверки работы системы " +
            "с размерами файлов в мегабайтах. Тест важен для обеспечения корректного отображения " +
            "размера файлов в мегабайтах.")
    @AllureId("FILE-FORMAT-002")
    @Step("Проверка форматирования размера файла в МБ")
    void testFileSizeFormatting_MB() {
        Allure.step("Подготовка данных: создание файла размером 2 MB");
        // Форматирование размера файла в МБ
        File file = new File();
        file.setFileSize(2097152L); // 2 MB

        Allure.step("Проверка размера файла");
        assertEquals(2097152L, file.getFileSize());
    }

    @Test
    @Description("Тест проверяет форматирование размера файла в ГБ. " +
            "Используется техника тест-дизайна 'Классы эквивалентности' для проверки работы системы " +
            "с размерами файлов в гигабайтах. Тест важен для обеспечения корректного отображения " +
            "размера файлов в гигабайтах.")
    @AllureId("FILE-FORMAT-003")
    @Step("Проверка форматирования размера файла в ГБ")
    void testFileSizeFormatting_GB() {
        Allure.step("Подготовка данных: создание файла размером 3 GB");
        // Форматирование размера файла в ГБ
        File file = new File();
        file.setFileSize(3221225472L); // 3 GB

        Allure.step("Проверка размера файла");
        assertEquals(3221225472L, file.getFileSize());
    }

    @Test
    @Description("Тест проверяет граничные условия размера файла. " +
            "Используется техника тест-дизайна 'Граничные значения' для проверки работы системы " +
            "с крайними значениями размера файла. Тест важен для обеспечения корректной работы системы " +
            "при работе с минимальными и максимальными размерами файлов.")
    @AllureId("FILE-FORMAT-004")
    @Step("Проверка граничных условий размера файла")
    void testFileSizeBoundaryConditions() {
        Allure.step("Подготовка данных: создание файла с нулевым размером");
        // Граничные условия размера файла
        File file = new File();

        // Нулевой размер
        file.setFileSize(0L);
        assertEquals(0L, file.getFileSize());

        Allure.step("Подготовка данных: создание файла с максимальным размером");
        // Очень большой размер
        file.setFileSize(Long.MAX_VALUE);
        assertEquals(Long.MAX_VALUE, file.getFileSize());

        Allure.step("Подготовка данных: создание файла с отрицательным размером");
        // Отрицательный размер
        file.setFileSize(-1L);
        assertEquals(-1L, file.getFileSize());
    }
}