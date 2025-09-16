package com.clouds.cloud_sprint;

import com.clouds.cloud_sprint.controller.SignupController;
import com.clouds.cloud_sprint.model.Users;
import com.clouds.cloud_sprint.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SignupControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private SignupController signupController;

    private Users testUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testUser = new Users();
        testUser.setUsername("testuser");
    }

    @Test
    void testShowSignupForm() {
        // Отображение формы регистрации
        String result = signupController.showSignupForm(model);
        assertEquals("signup", result);
        verify(model).addAttribute(eq("user"), any(Users.class));
    }

    @Test
    void testSignup_UserExists() {
        // Регистрация существующего пользователя
        when(userService.getUserByUsername("testuser")).thenReturn(Optional.of(testUser));

        String result = signupController.signup(testUser, bindingResult, model);

        assertEquals("signup", result);
        verify(model).addAttribute("error", "Пользователь с таким логином уже существует");
    }

    @Test
    void testSignup_ValidationErrors() {
        // Ошибки валидации при регистрации
        when(bindingResult.hasErrors()).thenReturn(true);

        String result = signupController.signup(testUser, bindingResult, model);

        assertEquals("signup", result);
        verify(model).addAttribute("error", "Проверьте введенные данные");
    }

    @Test
    void testSignup_Success() {
        // Успешная регистрация
        when(userService.getUserByUsername("testuser")).thenReturn(Optional.empty());
        when(bindingResult.hasErrors()).thenReturn(false);

        String result = signupController.signup(testUser, bindingResult, model);

        assertEquals("redirect:/login", result);
        verify(userService).createUser(testUser);
    }
}