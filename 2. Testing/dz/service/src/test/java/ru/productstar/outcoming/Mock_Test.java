package ru.productstar.outcoming;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.productstar.outcoming.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class Mock_Test {
    @Autowired
    private UserService userService;
    @MockBean
    private UserRepository userRepository;

    @Test
    void testSaveUser() {
        User user = new User("Test");
        when(userRepository.save(any(User.class))).thenReturn(user);
        User saved = userService.saveUser("Test");
        assertEquals("Test", saved.getName());
    }
}