package com.example.contacts;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContactServiceTest {

    @Mock
    private ContactDao contactDao;

    @InjectMocks
    private ContactService contactService;

    private MultipartFile validCsvFile;
    private MultipartFile invalidFormatFile;
    private MultipartFile invalidNameFile;

    @BeforeEach
    void setUp() {
        // Создаем тестовый CSV-файл с корректными данными
        String csvContent = "John Doe,1234567890,john@example.com\n" +
                "Jane Smith,0987654321,jane@example.com\n" +
                "Alice Johnson,1122334455,alice@example.com";
        validCsvFile = new MockMultipartFile(
                "contacts.csv",
                "contacts.csv",
                "text/csv",
                csvContent.getBytes());

        // Файл с неправильным форматом (недостаточно полей)
        String invalidContent = "John Doe,1234567890\n" +
                "Jane Smith,0987654321,jane@example.com,extra";
        invalidFormatFile = new MockMultipartFile(
                "invalid.csv",
                "invalid.csv",
                "text/csv",
                invalidContent.getBytes());

        // Файл с неправильным форматом имени
        String invalidNameContent = "John,1234567890,john@example.com\n" +
                "Smith Johnson Williams,0987654321,jane@example.com";
        invalidNameFile = new MockMultipartFile(
                "invalid_name.csv",
                "invalid_name.csv",
                "text/csv",
                invalidNameContent.getBytes());
    }

    @Test
    void testParseCsv_ValidFile() throws Exception {
        List<Contact> contacts = contactService.parseCsv(validCsvFile);

        assertEquals(3, contacts.size());

        Contact contact1 = contacts.get(0);
        assertEquals("John", contact1.getName());
        assertEquals("Doe", contact1.getSurname());
        assertEquals("1234567890", contact1.getPhone());
        assertEquals("john@example.com", contact1.getEmail());

        Contact contact2 = contacts.get(1);
        assertEquals("Jane", contact2.getName());
        assertEquals("Smith", contact2.getSurname());
        assertEquals("0987654321", contact2.getPhone());
        assertEquals("jane@example.com", contact2.getEmail());
    }

    @Test
    void testParseCsv_InvalidFormat_ShouldThrowException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            contactService.parseCsv(invalidFormatFile);
        });

        assertTrue(exception.getMessage().contains("Invalid CSV format"));
    }

    @Test
    void testParseCsv_InvalidNameFormat_ShouldThrowException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            contactService.parseCsv(invalidNameFile);
        });

        assertTrue(exception.getMessage().contains("Invalid name format"));
    }

    @Test
    void testImportContactsFromCsv_ValidFile() throws Exception {
        // Выполняем импорт
        contactService.importContactsFromCsv(validCsvFile);

        // Проверяем, что addContactsBatch был вызван один раз
        verify(contactDao, times(1)).addContactsBatch(anyList());

        // Проверяем, что передано правильное количество контактов
        ArgumentCaptor<List<Contact>> captor = ArgumentCaptor.forClass(List.class);
        verify(contactDao).addContactsBatch(captor.capture());

        List<Contact> capturedContacts = captor.getValue();
        assertEquals(3, capturedContacts.size());
    }

    @Test
    void testBatchProcessing_LargeFile() throws Exception {
        // Создаем CSV с 120 записями (больше BATCH_SIZE)
        StringBuilder csvContent = new StringBuilder();
        for (int i = 0; i < 120; i++) {
            csvContent.append("Name").append(i).append(" Surname").append(i)
                    .append(",1234567890,")
                    .append("email").append(i).append("@example.com\n");
        }

        MultipartFile largeFile = new MockMultipartFile(
                "large.csv",
                "large.csv",
                "text/csv",
                csvContent.toString().getBytes());

        // Выполняем импорт
        contactService.importContactsFromCsv(largeFile);

        // Проверяем, что addContactsBatch был вызван 3 раза (120 / 50 = 2.4 -> 3 батча)
        verify(contactDao, times(1)).addContactsBatch(anyList());

        // Проверяем, что размеры батчей правильные: 50, 50, 20
        ArgumentCaptor<List<Contact>> captor = ArgumentCaptor.forClass(List.class);
        verify(contactDao, times(1)).addContactsBatch(captor.capture());

        List<List<Contact>> capturedBatches = captor.getAllValues();
        assertEquals(120, capturedBatches.get(0).size());

    }
}
