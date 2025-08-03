package com.example.contacts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contacts")
public class ContactController {
    @Autowired
    private ContactService contactService;

    // GET /contacts - все контакты
    @GetMapping
    public List<Contact> getAllContacts() {
        return contactService.getAllContacts();
    }

    // GET /contact/{contactId} - конкретный контакт
    @GetMapping("/contact/{contactId}")
    public Contact getContactById(@PathVariable Long contactId) {
        return contactService.getContactById(contactId);
    }

    // POST /contacts - добавление контакта
    @PostMapping
    public Contact createContact(@RequestBody Contact contact) {
        return contactService.createContact(contact);
    }

    // PUT /contacts/{contactId} - обновление контакта
    @PutMapping("/{contactId}")
    public Contact updateContact(
            @PathVariable Long contactId,
            @RequestBody Contact contactDetails) {
        return contactService.updateContact(contactId, contactDetails);
    }
}
