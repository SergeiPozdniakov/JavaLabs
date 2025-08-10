package com.example.contacts;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class ContactService {
    private final ContactDao contactDao;

    public ContactService(ContactDao contactDao) {
        this.contactDao = contactDao;
    }

    public void importContactsFromCsv(MultipartFile File) throws IOException {
        List<Contact> contacts = parseCsv(File);
        contactDao.addContactsBatch(contacts);
    }

    List<Contact> parseCsv(MultipartFile file) throws IOException {
        List<Contact> contacts = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Пропускаем пустые строки
                if (line.trim().isEmpty()) {
                    continue;
                }

                // Парсим строку в формате "Имя Фамилия,Номер телефона,Email"
                String[] parts = line.split(",", 3);
                if (parts.length < 3) {
                    throw new IllegalArgumentException("Invalid CSV format: " + line);
                }

                String[] nameParts = parts[0].trim().split("\\s+", 2);
                if (nameParts.length < 2) {
                    throw new IllegalArgumentException("Invalid name format: " + parts[0]);
                }

                String name = nameParts[0];
                String surname = nameParts[1];
                String phone = parts[1].trim();
                String email = parts[2].trim();

                contacts.add(new Contact(name, surname, email, phone));
            }
        }
        return contacts;
    }
}
