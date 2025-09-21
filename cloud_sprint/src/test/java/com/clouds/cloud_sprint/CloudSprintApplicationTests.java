package com.clouds.cloud_sprint;

import io.qameta.allure.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@Epic("Основное приложение")
@Feature("Загрузка приложения")
@Story("Тестирование загрузки приложения Spring Boot")
@Owner("Команда разработки CloudSprint")
@Severity(SeverityLevel.NORMAL)
@SpringBootTest
public class CloudSprintApplicationTests {

	@Test
	@Description("Тест проверяет корректную загрузку приложения Spring Boot. " +
			"Используется техника тест-дизайна 'Классы эквивалентности' для проверки работы системы " +
			"с корректными данными. Тест важен для обеспечения корректной загрузки основного контекста " +
			"приложения Spring Boot.")
	@AllureId("APP-LOAD-001")
	@Step("Проверка загрузки контекста приложения")
    public void contextLoadsApp() {
		Allure.step("Загрузка контекста приложения Spring Boot");
	}

}
