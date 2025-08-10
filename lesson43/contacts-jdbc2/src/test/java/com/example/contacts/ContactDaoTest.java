package com.example.contacts;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContactDaoTest {

    @Mock
    private NamedParameterJdbcTemplate namedJdbcTemplate;

    @InjectMocks
    private ContactDao contactDao;

    @Captor
    private ArgumentCaptor<SqlParameterSource[]> batchParamsCaptor;

    @Test
    void testAddContactsBatch_SmallBatch() {
        // Создаем 30 контактов (меньше BATCH_SIZE)
        List<Contact> contacts = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            contacts.add(new Contact("Name" + i, "Surname" + i, "email" + i + "@example.com", "1234567890"));
        }

        // Выполняем батч-добавление
        contactDao.addContactsBatch(contacts);

        // Проверяем, что batchUpdate был вызван один раз
        verify(namedJdbcTemplate, times(1)).batchUpdate(
                anyString(),
                batchParamsCaptor.capture()
        );

        // Проверяем, что размер батча правильный
        SqlParameterSource[] params = batchParamsCaptor.getValue();
        assertEquals(30, params.length);
    }

    @Test
    void testAddContactsBatch_LargeBatch() {
        // Создаем 120 контактов (больше BATCH_SIZE)
        List<Contact> contacts = new ArrayList<>();
        for (int i = 0; i < 120; i++) {
            contacts.add(new Contact("Name" + i, "Surname" + i, "email" + i + "@example.com", "1234567890"));
        }

        // Выполняем батч-добавление
        contactDao.addContactsBatch(contacts);

        // Проверяем, что batchUpdate был вызван 3 раза (120 / 50 = 2.4 -> 3 батча)
        verify(namedJdbcTemplate, times(3)).batchUpdate(
                anyString(),
                batchParamsCaptor.capture()
        );

        // Проверяем, что размеры батчей правильные: 50, 50, 20
        List<SqlParameterSource[]> capturedBatches = batchParamsCaptor.getAllValues();
        assertEquals(50, capturedBatches.get(0).length);
        assertEquals(50, capturedBatches.get(1).length);
        assertEquals(20, capturedBatches.get(2).length);
    }
}
