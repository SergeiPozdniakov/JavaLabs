package com.example.contacts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/contacts")
public class ContactController {
    private final ContactDao contactDao;
    private final ContactService contactService;

    @Autowired
    public ContactController(ContactDao contactDao, ContactService contactService) {
        this.contactDao = contactDao;
        this.contactService = contactService;
    }

    @GetMapping
    public List<Contact> getAllContacts() {
        return contactDao.getAllContacts();
    }

    @GetMapping("/{id}")
    public Contact getContact(@PathVariable long id) {
        Contact contact = contactDao.getContact(id);
        if (contact == null) {
            throw new ContactNotFoundException("Contact with id " + id + " not found");
        }
        return contact;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public long addContact(@RequestBody Contact contact) {
        return contactDao.addContact(contact);
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadContacts(@RequestParam("file") MultipartFile file) {
        try {
            contactService.importContactsFromCsv(file);
            return ResponseEntity.ok("Successfully imported " + file.getOriginalFilename());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to process file: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/phone")
    public void updatePhoneNumber(@PathVariable long id, @RequestBody String phoneNumber) {
        contactDao.updatePhoneNumber(id, phoneNumber);
    }

    @PutMapping("/{id}/email")
    public void updateEmail(@PathVariable long id, @RequestBody String email) {
        contactDao.updateEmail(id, email);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteContact(@PathVariable long id) {
        contactDao.deleteContact(id);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ContactNotFoundException.class)
    public String contactNotFoundHandler(ContactNotFoundException ex) {
        return ex.getMessage();
    }
}

class ContactNotFoundException extends RuntimeException {
    public ContactNotFoundException(String message) {
        super(message);
    }
}
