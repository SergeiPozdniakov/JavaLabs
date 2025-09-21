package com.clouds.cloud_sprint;

import com.clouds.cloud_sprint.controller.SignupController;
import com.clouds.cloud_sprint.model.Users;
import com.clouds.cloud_sprint.services.UserService;
import io.qameta.allure.AllureId;
import io.qameta.allure.Description;
import io.qameta.allure.*;
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

@Epic("Аутентификация")
@Feature("Регистрация пользователей")
@Story("Тестирование контроллера регистрации")
@Owner("Команда разработки CloudSprint")
@Severity(SeverityLevel.CRITICAL)
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
    @Description("Тест проверяет отображение формы регистрации. " +
            "Используется техника тест-дизайна 'Классы эквивалентности' для проверки работы системы " +
            "с корректными данными. Тест важен для обеспечения корректного отображения формы регистрации.")
    @AllureId("AUTH-SIGNUP-001")
    @Step("Проверка отображения формы регистрации")
    void testShowSignupForm() {
        Allure.step("Выполнение запроса на получение формы регистрации");
        // Отображение формы регистрации
        String result = signupController.showSignupForm(model);

        Allure.step("Проверка результата");
        assertEquals("signup", result);
        verify(model).addAttribute(eq("user"), any(Users.class));
    }

    @Test
    @Description("Тест проверяет регистрацию существующего пользователя. " +
            "Используется техника тест-дизайна 'Граничные значения' для проверки работы системы " +
            "с существующими пользователями. Тест важен для обеспечения корректной обработки ошибок при " +
            "попытке зарегистрировать существующего пользователя.")
    @AllureId("AUTH-SIGNUP-002")
    @Step("Проверка регистрации существующего пользователя")
    void testSignup_UserExists() {
        Allure.step("Подготовка данных: создание существующего пользователя");
        // Регистрация существующего пользователя
        when(userService.getUserByUsername("testuser")).thenReturn(Optional.of(testUser));

        Allure.step("Выполнение регистрации существующего пользователя");
        String result = signupController.signup(testUser, bindingResult, model);

        Allure.step("Проверка результата");
        assertEquals("signup", result);
        verify(model).addAttribute("error", "Пользователь с таким логином уже существует");
    }

    @Test
    @Description("Тест проверяет ошибки валидации при регистрации. " +
            "Используется техника тест-дизайна 'Граничные значения' для проверки работы системы " +
            "с ошибками валидации. Тест важен для обеспечения корректной обработки ошибок валидации " +
            "при регистрации пользователя.")
    @AllureId("AUTH-SIGNUP-003")
    @Step("Проверка ошибок валидации при регистрации")
    void testSignup_ValidationErrors() {
        Allure.step("Подготовка данных: имитация ошибок валидации");
        // Ошибки валидации при регистрации
        when(bindingResult.hasErrors()).thenReturn(true);

        Allure.step("Выполнение регистрации с ошибками валидации");
        String result = signupController.signup(testUser, bindingResult, model);

        Allure.step("Проверка результата");
        assertEquals("signup", result);
        verify(model).addAttribute("error", "Проверьте введенные данные");
    }

    @Test
    @Description("Тест проверяет успешную регистрацию пользователя. " +
            "Используется техника тест-дизайна 'Классы эквивалентности' для проверки работы системы " +
            "с корректными данными. Тест важен для обеспечения корректной регистрации пользователя.")
    @AllureId("AUTH-SIGNUP-004")
    @Step("Проверка успешной регистрации пользователя")
    void testSignup_Success() {
        Allure.step("Подготовка данных: создание нового пользователя");
        // Успешная регистрация
        when(userService.getUserByUsername("testuser")).thenReturn(Optional.empty());
        when(bindingResult.hasErrors()).thenReturn(false);

        Allure.step("Выполнение регистрации нового пользователя");
        String result = signupController.signup(testUser, bindingResult, model);

        Allure.step("Проверка результата");
        assertEquals("redirect:/login", result);
        verify(userService).createUser(testUser);
    }
}