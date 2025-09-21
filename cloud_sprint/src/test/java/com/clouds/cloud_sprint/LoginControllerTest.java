package com.clouds.cloud_sprint;

import com.clouds.cloud_sprint.controller.LoginController;
import com.clouds.cloud_sprint.security.SecurityConfig;
import io.qameta.allure.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Epic("Аутентификация")
@Feature("Вход в систему")
@Story("Тестирование контроллера логина")
@Owner("Команда разработки CloudSprint")
@Severity(SeverityLevel.CRITICAL)
@WebMvcTest(LoginController.class)
@Import(SecurityConfig.class) // Импортируем конфигурацию безопасности
class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Description("Тест проверяет отображение формы входа. " +
            "Используется техника тест-дизайна 'Классы эквивалентности' для проверки работы системы " +
            "с корректными данными. Тест важен для обеспечения корректного отображения формы входа.")
    @AllureId("AUTH-LOGIN-001")
    @Step("Проверка отображения формы входа")
    void testShowLoginForm() throws Exception {
        Allure.step("Выполнение запроса на получение формы входа");
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk()) // Ожидаем статус 200
                .andExpect(view().name("login")) // Ожидаем имя представления "login"
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Вход в облачное хранилище"))); // Дополнительная проверка содержимого страницы
    }
}