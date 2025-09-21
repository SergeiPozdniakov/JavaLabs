package com.clouds.cloud_sprint;

import com.clouds.cloud_sprint.model.FileUploadProgressListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import io.qameta.allure.*;

@Epic("Файловые операции")
@Feature("Отслеживание прогресса загрузки")
@Story("Тестирование слушателя прогресса загрузки")
@Owner("Команда разработки CloudSprint")
@Severity(SeverityLevel.NORMAL)
class FileUploadProgressListenerTest {

    private FileUploadProgressListener progressListener;

    @BeforeEach
    void setUp() {
        progressListener = new FileUploadProgressListener();
    }

    @Test
    @Description("Тест проверяет начальные значения слушателя прогресса загрузки. " +
            "Используется техника тест-дизайна 'Классы эквивалентности' для проверки работы системы " +
            "с начальными значениями. Тест важен для обеспечения корректной инициализации слушателя.")
    @AllureId("PROGRESS-LISTENER-001")
    @Step("Проверка начальных значений")
    void testInitialState() {
        Allure.step("Проверка начальных значений слушателя прогресса");
        // Проверка начальных значений
        assertEquals(0, progressListener.getBytesRead(),
                "ожидается 0 байт");
        assertEquals(0, progressListener.getPercentComplete(),
                "ожидается 0 процентов");
    }

    @Test
    @Description("Тест проверяет обновление прогресса для файла нулевого размера. " +
            "Используется техника тест-дизайна 'Граничные значения' для проверки работы системы " +
            "с нулевым размером файла. Тест важен для обеспечения корректной обработки нулевых " +
            "размеров файлов.")
    @AllureId("PROGRESS-LISTENER-002")
    @Step("Проверка обновления прогресса для файла нулевого размера")
    void testUpdateEmptyFile() {
        Allure.step("Обновление прогресса для файла нулевого размера");
        // Попытка обновить с нулевым размером файла
        progressListener.update(100, 0);

        assertEquals(0, progressListener.getPercentComplete(),
                "Процент должен остаться 0 при нулевом размере файла");
    }

    @Test
    @Description("Тест проверяет корректное обновление прогресса для рабочего состояния. " +
            "Используется техника тест-дизайна 'Классы эквивалентности' для проверки работы системы " +
            "с корректными данными. Тест важен для обеспечения корректного обновления прогресса " +
            "загрузки.")
    @AllureId("PROGRESS-LISTENER-003")
    @Step("Проверка корректного обновления прогресса для рабочего состояния")
    void testUpdateWorkStata() {
        Allure.step("Обновление прогресса для рабочего состояния");
        progressListener.update(25, 100);

        assertEquals(25, progressListener.getPercentComplete(),
                "должно быть 25%");
        assertEquals(25, progressListener.getBytesRead(),
                "Должно быть 25 байт");
    }

    @Test
    @Description("Тест проверяет корректное обновление прогресса для больших значений. " +
            "Используется техника тест-дизайна 'Классы эквивалентности' для проверки работы системы " +
            "с большими значениями. Тест важен для обеспечения корректного обновления прогресса " +
            "загрузки для больших файлов.")
    @AllureId("PROGRESS-LISTENER-004")
    @Step("Проверка корректного обновления прогресса для больших значений")
    void testUpdateWithLargeValues() {
        Allure.step("Обновление прогресса для больших значений");
        progressListener.update(5_000_000L, 10_000_000L);

        assertEquals(50, progressListener.getPercentComplete(),
                "должно быть 50%");
    }

    @Test
    @Description("Тест проверяет корректное завершение загрузки. " +
            "Используется техника тест-дизайна 'Граничные значения' для проверки работы системы " +
            "с полным завершением загрузки. Тест важен для обеспечения корректного завершения " +
            "загрузки файлов.")
    @AllureId("PROGRESS-LISTENER-005")
    @Step("Проверка полного завершения загрузки")
    void testComplete() {
        Allure.step("Обновление прогресса до 100%");
        progressListener.update(100, 100);

        assertEquals(100, progressListener.getPercentComplete(),
                "должно быть 100%");
    }

    @Test
    @Description("Тест проверяет защиту от переполнения прогресса. " +
            "Используется техника тест-дизайна 'Граничные значения' для проверки работы системы " +
            "с переполнением прогресса. Тест важен для обеспечения корректной обработки переполнения " +
            "прогресса загрузки.")
    @AllureId("PROGRESS-LISTENER-006")
    @Step("Проверка защиты от переполнения")
    void testUpdateWithOverflow() {
        Allure.step("Обновление прогресса с переполнением");
        progressListener.update(150, 100);

        assertEquals(100, progressListener.getPercentComplete(),
                "должно быть 100%");
    }


    @Test
    @Description("Тест проверяет множественные обновления прогресса. " +
            "Используется техника тест-дизайна 'Классы эквивалентности' для проверки работы системы " +
            "с последовательными обновлениями прогресса. Тест важен для обеспечения корректного " +
            "отслеживания прогресса при нескольких обновлениях.")
    @AllureId("PROGRESS-LISTENER-007")
    @Step("Проверка множественных обновлений прогресса")
    void testMultipleUpdates() {
        Allure.step("Последовательные обновления прогресса");
        // Последовательные обновления прогресса
        progressListener.update(10, 100);
        assertEquals(10, progressListener.getPercentComplete(),
                "должно быть 10%");
        progressListener.update(50, 100);
        assertEquals(50, progressListener.getPercentComplete(),
                "должно быть 50%");
        progressListener.update(100, 100);
        assertEquals(100, progressListener.getPercentComplete(),
                "должно быть 100%");
    }

    @Test
    @Description("Тест проверяет сброс состояния после обновления. " +
            "Используется техника тест-дизайна 'Классы эквивалентности' для проверки работы системы " +
            "с сбросом состояния. Тест важен для обеспечения корректного сброса состояния прогресса " +
            "после завершения загрузки.")
    @AllureId("PROGRESS-LISTENER-008")
    @Step("Проверка сброса состояния после обновления")
    void testResetAfterUpdate() {
        Allure.step("Обновление прогресса и последующий сброс");
        progressListener.update(50, 100);

        // Сбрасываем
        progressListener.reset();

        // Проверяем сброс всех полей
        assertEquals(0, progressListener.getBytesRead(),
                " должно быть 0 после сброса");
        assertEquals(0, progressListener.getPercentComplete(),
                "должно быть 0% после сброса");
    }

    @Test
    @Description("Тест проверяет сброс из начального состояния. " +
            "Используется техника тест-дизайна 'Классы эквивалентности' для проверки работы системы " +
            "с сбросом из начального состояния. Тест важен для обеспечения корректного сброса " +
            "состояния прогресса из начального состояния.")
    @AllureId("PROGRESS-LISTENER-009")
    @Step("Проверка сброса из начального состояния")
    void testResetFromInitialState() {
        Allure.step("Сброс из начального состояния");
        // Сбрасываем без предварительных обновлений
        progressListener.reset();

        assertEquals(0, progressListener.getBytesRead(),
                " должно быть 0 после сброса");
        assertEquals(0, progressListener.getPercentComplete(),
                "должно быть 0% после сброса");
    }

    @Test
    @Description("Тест проверяет минимальный прогресс загрузки. " +
            "Используется техника тест-дизайна 'Граничные значения' для проверки работы системы " +
            "с минимальным прогрессом. Тест важен для обеспечения корректного отображения минимального " +
            "прогресса загрузки.")
    @AllureId("PROGRESS-LISTENER-010")
    @Step("Проверка минимального прогресса")
    void testMinimalProgress() {
        Allure.step("Обновление прогресса для минимального значения");
        progressListener.update(1, 100);
        assertEquals(1, progressListener.getPercentComplete(),
                "1 байт из 100 должен давать 1%");
    }
}