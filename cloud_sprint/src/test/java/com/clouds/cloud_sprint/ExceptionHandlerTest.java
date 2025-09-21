package com.clouds.cloud_sprint;

import com.clouds.cloud_sprint.GlobalExceptionHandler;
import com.clouds.cloud_sprint.controller.HomeController;
import com.clouds.cloud_sprint.services.FileService;
import io.qameta.allure.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Epic("Обработка исключений")
@Feature("Обработка ошибок при работе с файлами")
@Story("Тестирование обработки исключений в контроллере")
@Owner("Команда разработки CloudSprint")
@Severity(SeverityLevel.CRITICAL)
@WebMvcTest(HomeController.class)
@Import(GlobalExceptionHandler.class) // ← Добавлено!
class ExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private FileService fileService;

    @Test
    @Description("Тест проверяет корректную обработку исключения при попытке загрузить несуществующий файл. " +
                "Используется техника тест-дизайна 'Граничные значения' для проверки работы системы " +
                "с некорректными данными (ID файла, которого нет в системе). Тест важен для обеспечения " +
                "корректной обработки ошибок и отправки пользователю понятного сообщения.")
    @AllureId("EXCEPTION-001")
    @Step("Проверка обработки исключения при попытке загрузить несуществующий файл")
    @WithMockUser(username = "testuser")
    void testDownloadFile_NotFound() throws Exception {
        Allure.step("Подготовка данных: создание мока для несуществующего файла");
        when(fileService.getFileById(999L)).thenThrow(new RuntimeException("File not found"));

        Allure.step("Выполнение запроса на скачивание несуществующего файла");
        mockMvc.perform(get("/home/download/999"))
                .andExpect(status().is5xxServerError())
                .andExpect(content().string("File not found")); // ← Опционально
    }

    @Test
    @Description("Тест проверяет корректную обработку исключения при попытке загрузить непрочитаемый файл. " +
            "Используется техника тест-дизайна 'Граничные значения' для проверки работы системы " +
            "с некорректными данными (файл, который не может быть прочитан). Тест важен для обеспечения " +
            "корректной обработки ошибок и отправки пользователю понятного сообщения.")
    @AllureId("EXCEPTION-002")
    @Step("Проверка обработки исключения при попытке загрузить непрочитаемый файл")
    @WithMockUser(username = "testuser")
    void testDownloadFile_FileNotReadable() throws Exception {
        Allure.step("Подготовка данных: создание мока для непрочитаемого файла");
        when(fileService.getFileById(1L)).thenThrow(new RuntimeException("File is not readable"));

        Allure.step("Выполнение запроса на скачивание непрочитаемого файла");
        mockMvc.perform(get("/home/download/1"))
                .andExpect(status().is5xxServerError())
                .andExpect(content().string("File is not readable"));
    }
}