package com.clouds.cloud_sprint;


import com.clouds.cloud_sprint.controller.ProgressController;
import com.clouds.cloud_sprint.model.FileUploadProgressListener;
import io.qameta.allure.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@Epic("Файловые операции")
@Feature("Отслеживание прогресса загрузки")
@Story("Тестирование контроллера прогресса загрузки")
@Owner("Команда разработки CloudSprint")
@Severity(SeverityLevel.NORMAL)
@ExtendWith(MockitoExtension.class)
class ProgressControllerTest {

    @Mock
    private FileUploadProgressListener progressListener;

    @InjectMocks
    private ProgressController progressController;

    @Test
    @Description("Тест проверяет метод trackUploadProgress() для создания SSE соединения. " +
            "Используется техника тест-дизайна 'Классы эквивалентности' для проверки работы системы " +
            "с корректными данными. Тест важен для обеспечения корректного отслеживания прогресса " +
            "загрузки файла в реальном времени.")
    @AllureId("PROGRESS-CONTROLLER-001")
    @Step("Проверка метода trackUploadProgress()")
    void testTrackUploadProgress() throws Exception {
        Allure.step("Подготовка данных: настройка мока для прогресса загрузки");
        // при первом вызове вернет 0, при втором - 100
        when(progressListener.getPercentComplete()).thenReturn(0, 100);

        Allure.step("Создание SSE соединения");
        SseEmitter emitter = progressController.trackUploadProgress();

        Allure.step("Проверка работы SSE соединения");
        // время на SSE соединение, проверку итераций событий
        Thread.sleep(200);

        verify(progressListener, atLeastOnce()).getPercentComplete();
        verify(progressListener).reset(); // Контроллер после окончания
                                          // загрузки сбрасывает полосу
    }

    @Test
    @Description("Тест проверяет метод trackUploadProgress() на постепенное обновление прогресса. " +
            "Используется техника тест-дизайна 'Классы эквивалентности' для проверки работы системы " +
            "с корректными данными. Тест важен для обеспечения корректного постепенного обновления " +
            "прогресса загрузки файла.")
    @AllureId("PROGRESS-CONTROLLER-002")
    @Step("Проверка постепенного обновления прогресса")
    void testTrackUploadProgressMultiple() throws Exception {
        Allure.step("Подготовка данных: настройка мока для постепенного обновления прогресса");
        when(progressListener.getPercentComplete()).thenReturn(0, 25, 50, 75, 100);

        Allure.step("Создание SSE соединения");
        SseEmitter emitter = progressController.trackUploadProgress();

        Allure.step("Проверка постепенного обновления прогресса");
        Thread.sleep(500);

        verify(progressListener, atLeast(4)).getPercentComplete();
        verify(progressListener).reset();
    }

    @Test
    @Description("Тест проверяет метод trackUploadProgress() на остановку проверок после 100 процентов. " +
            "Используется техника тест-дизайна 'Граничные значения' для проверки работы системы " +
            "в состоянии полного завершения загрузки. Тест важен для обеспечения корректной остановки " +
            "проверок после завершения загрузки файла.")
    @AllureId("PROGRESS-CONTROLLER-003")
    @Step("Проверка остановки проверок после 100 процентов")
    void testTrackUploadProgressCompletesAt100() throws Exception {
        Allure.step("Подготовка данных: настройка мока для завершения загрузки");
        when(progressListener.getPercentComplete()).thenReturn(99, 100);

        Allure.step("Создание SSE соединения");
        SseEmitter emitter = progressController.trackUploadProgress();

        Allure.step("Проверка остановки проверок после 100%");
        Thread.sleep(200);

        verify(progressListener, times(2)).getPercentComplete();
        verify(progressListener).reset();
    }

    @Test
    @Description("Тест проверяет метод trackUploadProgress() при мгновенной загрузке. " +
            "Используется техника тест-дизайна 'Граничные значения' для проверки работы системы " +
            "с мгновенной загрузкой. Тест важен для обеспечения корректной работы системы при " +
            "мгновенной загрузке файла.")
    @AllureId("PROGRESS-CONTROLLER-004")
    @Step("Проверка метода trackUploadProgress() при мгновенной загрузке")
    void testTrackUploadProgressFastComplite() throws Exception {
        Allure.step("Подготовка данных: настройка мока для мгновенной загрузки");
        when(progressListener.getPercentComplete()).thenReturn(100);

        Allure.step("Создание SSE соединения");
        SseEmitter emitter = progressController.trackUploadProgress();

        Allure.step("Проверка работы SSE соединения при мгновенной загрузке");
        Thread.sleep(100);

        verify(progressListener, atLeastOnce()).getPercentComplete();
        verify(progressListener).reset();
    }

    @Test
    @Description("Тест проверяет метод trackUploadProgress() при возникновении исключения. " +
            "Используется техника тест-дизайна 'Граничные значения' для проверки работы системы " +
            "с исключениями. Тест важен для обеспечения корректной обработки исключений при " +
            "отслеживании прогресса загрузки.")
    @AllureId("PROGRESS-CONTROLLER-005")
    @Step("Проверка метода trackUploadProgress() при возникновении исключения")
    void testTrackUploadProgressWithException() throws Exception {
        Allure.step("Подготовка данных: настройка мока для возникновения исключения");
        when(progressListener.getPercentComplete()).thenThrow(new RuntimeException("Test error"));

        Allure.step("Создание SSE соединения");
        SseEmitter emitter = progressController.trackUploadProgress();

        Allure.step("Проверка обработки исключения");
        Thread.sleep(100);

        // Проверяем, что исключение было обработано
        assertNotNull(emitter);
    }

    @Test
    @Description("Тест проверяет корректное создание эмиттера в методе trackUploadProgress(). " +
            "Используется техника тест-дизайна 'Классы эквивалентности' для проверки работы системы " +
            "с корректными данными. Тест важен для обеспечения корректного создания SSE эмиттера.")
    @AllureId("PROGRESS-CONTROLLER-006")
    @Step("Проверка корректного создания эмиттера")
    void testTrackUploadProgressEmitterCreation() {
        Allure.step("Создание SSE эмиттера");
        SseEmitter emitter = progressController.trackUploadProgress();

        Allure.step("Проверка создания эмиттера");
        assertNotNull(emitter);
        assertTrue(emitter instanceof SseEmitter);
    }
}
