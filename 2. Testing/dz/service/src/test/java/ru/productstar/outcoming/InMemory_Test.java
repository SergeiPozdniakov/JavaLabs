package ru.productstar.outcoming;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;
import ru.productstar.outcoming.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ComponentScan(basePackages = "ru.productstar.outcoming")
@TestPropertySource(locations = "classpath:application-test.properties")
public class InMemory_Test {
    @Autowired
    private UserService userService;

    @Test
    void testSaveAndGetUser() {
        User saved = userService.saveUser("Test");
        User found = userService.getUser(saved.getId());
        assertEquals("Test", found.getName());
    }
}