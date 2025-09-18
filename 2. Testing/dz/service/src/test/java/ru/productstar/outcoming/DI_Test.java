package ru.productstar.outcoming;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.productstar.outcoming.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DI_Test {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;

    @Test
    void testSaveUser() {
        User user = new User("Test");
        when(userRepository.save(any(User.class))).thenReturn(user);
        User saved = userService.saveUser("Test");
        assertEquals("Test", saved.getName());
    }
}