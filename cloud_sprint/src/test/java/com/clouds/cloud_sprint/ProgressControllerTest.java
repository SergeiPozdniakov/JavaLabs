package com.clouds.cloud_sprint;


import com.clouds.cloud_sprint.controller.ProgressController;
import com.clouds.cloud_sprint.model.FileUploadProgressListener;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProgressControllerTest {

    @Mock
    private FileUploadProgressListener progressListener;

    @InjectMocks
    private ProgressController progressController;

    //проверка метода trackUploadProgress()
    // метод создает Server-Sent Events (SSE) соединение для отслеживания
    // прогресса загрузки файла в реальном времени
    @Test
    void testTrackUploadProgress() throws Exception {
        // при первом вызове вернет 0, при втором - 100
        when(progressListener.getPercentComplete()).thenReturn(0, 100);

        SseEmitter emitter = progressController.trackUploadProgress();

        // время на SSE соединение, проверку итераций событий
        Thread.sleep(200);

        verify(progressListener, atLeastOnce()).getPercentComplete();
        verify(progressListener).reset(); // Контроллер после окончания
                                          // загрузки сбрасывает полосу
    }

    // проверка метода trackUploadProgress() на постепенное обновление прогресса
    @Test
    void testTrackUploadProgressMultiple() throws Exception {
        when(progressListener.getPercentComplete()).thenReturn(0, 25, 50, 75, 100);

        SseEmitter emitter = progressController.trackUploadProgress();

        Thread.sleep(500);

        verify(progressListener, atLeast(4)).getPercentComplete();
        verify(progressListener).reset();
    }

    // проверка метода trackUploadProgress() на остановку проверок после 100 процентов
    @Test
    void testTrackUploadProgressCompletesAt100() throws Exception {
        when(progressListener.getPercentComplete()).thenReturn(99, 100);

        SseEmitter emitter = progressController.trackUploadProgress();

        Thread.sleep(200);

        verify(progressListener, times(2)).getPercentComplete();
        verify(progressListener).reset();
    }

    // проверка метода trackUploadProgress() при мгновенной загрузке
    @Test
    void testTrackUploadProgressFastComplite() throws Exception {
        when(progressListener.getPercentComplete()).thenReturn(100);

        SseEmitter emitter = progressController.trackUploadProgress();

        Thread.sleep(100);

        verify(progressListener, atLeastOnce()).getPercentComplete();
        verify(progressListener).reset();
    }

    // проверка отработки исключений
    @Test
    void testTrackUploadProgressWithException() throws Exception {
        when(progressListener.getPercentComplete()).thenThrow(new RuntimeException("Test error"));

        SseEmitter emitter = progressController.trackUploadProgress();

        Thread.sleep(100);

        // Проверяем, что исключение было обработано
        assertNotNull(emitter);
    }

    // проверка корректного создания эмиттера
    @Test
    void testTrackUploadProgressEmitterCreation() {
        SseEmitter emitter = progressController.trackUploadProgress();

        assertNotNull(emitter);
        assertTrue(emitter instanceof SseEmitter);
    }
}
