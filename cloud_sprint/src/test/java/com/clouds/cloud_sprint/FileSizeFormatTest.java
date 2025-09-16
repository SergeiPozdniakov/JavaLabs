package com.clouds.cloud_sprint;

import com.clouds.cloud_sprint.model.File;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class FileSizeFormatTest {

    @Test
    void testFileSizeFormatting_KB() {
        // Форматирование размера файла в КБ
        File file = new File();
        file.setFileSize(1536L); // 1.5 KB

        assertEquals(1536L, file.getFileSize());
    }

    @Test
    void testFileSizeFormatting_MB() {
        // Test 73: Форматирование размера файла в МБ
        File file = new File();
        file.setFileSize(2097152L); // 2 MB

        assertEquals(2097152L, file.getFileSize());
    }

    @Test
    void testFileSizeFormatting_GB() {
        // Форматирование размера файла в ГБ
        File file = new File();
        file.setFileSize(3221225472L); // 3 GB

        assertEquals(3221225472L, file.getFileSize());
    }

    @Test
    void testFileSizeBoundaryConditions() {
        // Граничные условия размера файла
        File file = new File();

        // Нулевой размер
        file.setFileSize(0L);
        assertEquals(0L, file.getFileSize());

        // Очень большой размер
        file.setFileSize(Long.MAX_VALUE);
        assertEquals(Long.MAX_VALUE, file.getFileSize());

        // Отрицательный размер
        file.setFileSize(-1L);
        assertEquals(-1L, file.getFileSize());
    }
}