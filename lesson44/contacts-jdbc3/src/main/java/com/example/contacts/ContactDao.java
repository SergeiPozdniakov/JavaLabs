package com.example.contacts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public class ContactDao {

    private final ContactRepository contactRepository;

    @Autowired
    public ContactDao(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    public List<Contact> getAllContacts() {
        return contactRepository.findAll();
    }

    public Contact getContact(long contactId) {
        return contactRepository.findById(contactId).orElse(null);
    }

    @Transactional
    public long addContact(Contact contact) {
        Contact savedContact = contactRepository.save(contact);
        return savedContact.getId();
    }

    @Transactional
    public void addContactsBatch(List<Contact> contacts) {
        contactRepository.saveAll(contacts);
    }

    @Transactional
    public void updatePhoneNumber(long contactId, String phoneNumber) {
        Optional<Contact> contactOptional = contactRepository.findById(contactId);
        if (contactOptional.isPresent()) {
            Contact contact = contactOptional.get();
            contact.setPhone(phoneNumber);
            contactRepository.save(contact);
        }
    }

    @Transactional
    public void updateEmail(long contactId, String email) {
        Optional<Contact> contactOptional = contactRepository.findById(contactId);
        if (contactOptional.isPresent()) {
            Contact contact = contactOptional.get();
            contact.setEmail(email);
            contactRepository.save(contact);
        }
    }

    @Transactional
    public void deleteContact(long contactId) {
        contactRepository.deleteById(contactId);
    }
}