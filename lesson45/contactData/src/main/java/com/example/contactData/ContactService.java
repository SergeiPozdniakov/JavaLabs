package com.example.contactData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
public class ContactService {

    private final ContactRepository contactRepository;

    @Autowired
    public ContactService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    // Добавление контакта
    public Contact addContact(Contact contact) {
        return contactRepository.save(contact);
    }

    // Обновление телефона (транзакция нужна для @Modifying)
    @Transactional
    public void updatePhone(Long id, String newPhone) {
        contactRepository.updatePhone(id, newPhone);
    }

    @Transactional
    public void updateEmail(Long id, String newEmail) {
        contactRepository.updateEmail(id, newEmail);
    }

    public Optional<Contact> findById(Long id) {
        return contactRepository.findById(id);
    }

}
