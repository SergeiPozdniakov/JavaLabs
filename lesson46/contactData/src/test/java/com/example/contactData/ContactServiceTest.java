package com.example.contactData;

import com.example.contactData.Contact;
import com.example.contactData.ContactRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContactServiceTest {

    @Mock
    private ContactRepository contactRepository;

    @InjectMocks
    private ContactService contactService;

    private Contact testContact;

    @BeforeEach
    void setUp() {
        testContact = new Contact();
        testContact.setFirstName("John");
        testContact.setLastName("Doe");
        testContact.setPhone("123-456");
        testContact.setEmail("john@example.com");
    }

    @Test
    void shouldAddContactSuccessfully() {

        Contact savedContact = new Contact();
        savedContact.setId(1L);
        savedContact.setFirstName(testContact.getFirstName());
        savedContact.setLastName(testContact.getLastName());
        savedContact.setPhone(testContact.getPhone());
        savedContact.setEmail(testContact.getEmail());

        when(contactRepository.save(any(Contact.class))).thenReturn(savedContact);


        Contact result = contactService.addContact(testContact);


        assertNotNull(result.getId());
        assertEquals("John", result.getFirstName());
        verify(contactRepository, times(1)).save(testContact);
    }

    @Test
    void shouldUpdatePhoneNumber() {

        Long contactId = 1L;
        String newPhone = "999-888";


        contactService.updatePhone(contactId, newPhone);


        verify(contactRepository, times(1)).updatePhone(contactId, newPhone);
    }

    @Test
    void shouldUpdateEmail() {

        Long contactId = 1L;
        String newEmail = "new@example.com";


        contactService.updateEmail(contactId, newEmail);


        verify(contactRepository, times(1)).updateEmail(contactId, newEmail);
    }

    @Test
    void shouldFindContactById() {

        Long contactId = 1L;
        when(contactRepository.findById(contactId)).thenReturn(Optional.of(testContact));


        Optional<Contact> result = contactService.findById(contactId);


        assertTrue(result.isPresent());
        assertEquals("John", result.get().getFirstName());
        verify(contactRepository, times(1)).findById(contactId);
    }
}