package com.clouds.cloud_sprint;

import com.clouds.cloud_sprint.model.File;
import com.clouds.cloud_sprint.model.Users;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FileTest {

    private File file;
    private Users user;

    @BeforeEach
    void setUp() {
        file = new File();
        user = new Users();
    }

    @Test
    void testFileId() {
        // Test 13: Установка и получение ID файла
        file.setId(1L);
        assertEquals(1L, file.getId());
    }

    @Test
    void testFileName() {
        // Test 14: Установка и получение имени файла
        file.setFileName("test.txt");
        assertEquals("test.txt", file.getFileName());
    }

    @Test
    void testContentType() {
        // Test 15: Установка и получение типа контента
        file.setContentType("text/plain");
        assertEquals("text/plain", file.getContentType());
    }

    @Test
    void testFileSize() {
        // Test 16: Установка и получение размера файла
        file.setFileSize(1024L);
        assertEquals(1024L, file.getFileSize());
    }

    @Test
    void testFilePath() {
        // Test 17: Установка и получение пути файла
        file.setFilePath("/path/to/file.txt");
        assertEquals("/path/to/file.txt", file.getFilePath());
    }

    @Test
    void testFileUser() {
        // Test 18: Установка и получение пользователя файла
        file.setUser(user);
        assertEquals(user, file.getUser());
    }
}
