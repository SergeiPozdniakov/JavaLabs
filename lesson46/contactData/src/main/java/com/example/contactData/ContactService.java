package com.example.contactData;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
public class ContactService {
    // Добавлены правильные импорты для логирования
    private static final Logger logger = LoggerFactory.getLogger(ContactService.class);

    private final ContactRepository contactRepository;

    @Autowired
    public ContactService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
        logger.info("ContactService initialized");
        logger.debug("ContactService debug message");
    }

    // Добавление контакта
    public Contact addContact(Contact contact) {
        logger.info("Adding new contact: {} {}", contact.getFirstName(), contact.getLastName());
        logger.debug("Adding new contact with details: {}", contact);
        return contactRepository.save(contact);
    }

    // Обновление телефона
    @Transactional
    public void updatePhone(Long id, String newPhone) {
        logger.info("Updating phone for contact with id: {}", id);
        logger.debug("Updating phone for contact with id: {} to new phone: {}", id, newPhone);
        contactRepository.updatePhone(id, newPhone);
    }

    @Transactional
    public void updateEmail(Long id, String newEmail) {
        logger.info("Updating email for contact with id: {}", id);
        logger.debug("Updating email for contact with id: {} to new email: {}", id, newEmail);
        contactRepository.updateEmail(id, newEmail);
    }

    public Optional<Contact> findById(Long id) {
        logger.info("Finding contact with id: {}", id);
        logger.debug("Finding contact with id: {}", id);
        return contactRepository.findById(id);
    }
}