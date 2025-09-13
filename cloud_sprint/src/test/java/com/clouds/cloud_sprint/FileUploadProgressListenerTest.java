package com.clouds.cloud_sprint;

import com.clouds.cloud_sprint.model.FileUploadProgressListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FileUploadProgressListenerTest {

    private FileUploadProgressListener progressListener;

    @BeforeEach
    void setUp() {
        progressListener = new FileUploadProgressListener();
    }

    // 1. Проверка начальных значений
    @Test
    void testInitialState() {
        // Проверка начальных значений
        assertEquals(0, progressListener.getBytesRead(),
                "ожидается 0 байт");
        assertEquals(0, progressListener.getPercentComplete(),
                "ожидается 0 процентов");
    }

    // 2. Тест проверки чтения объекта нулевого размера
    @Test
    void testUpdateEmptyFile() {
        // Попытка обновить с нулевым размером файла
        progressListener.update(100, 0);

        assertEquals(0, progressListener.getPercentComplete(),
                "Процент должен остаться 0 при нулевом размере файла");
    }

    // 3. Тест проверки рабочего состояния
    @Test
    void testUpdateWorkStata() {
        progressListener.update(25, 100);

        assertEquals(25, progressListener.getPercentComplete(),
                "должно быть 25%");
        assertEquals(25, progressListener.getBytesRead(),
                "Должно быть 25 байт");
    }

    // 4. Тест проверки больших значений
    @Test
    void testUpdateWithLargeValues() {
        progressListener.update(5_000_000L, 10_000_000L);

        assertEquals(50, progressListener.getPercentComplete(),
                "должно быть 50%");
    }

     // 5. Тест полного завершения загрузки.
    @Test
    void testComplete() {
        progressListener.update(100, 100);

        assertEquals(100, progressListener.getPercentComplete(),
                "должно быть 100%");
    }

     //6. Тест защиты от переполнения.
    @Test
    void testUpdateWithOverflow() {
        progressListener.update(150, 100);

        assertEquals(100, progressListener.getPercentComplete(),
                "должно быть 100%");
    }


     //7. Тест множественных обновлений.
    @Test
    void testMultipleUpdates() {
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

     // 8. Тест сброса состояния после обновления.
    @Test
    void testResetAfterUpdate() {
        progressListener.update(50, 100);

        // Сбрасываем
        progressListener.reset();

        // Проверяем сброс всех полей
        assertEquals(0, progressListener.getBytesRead(),
                " должно быть 0 после сброса");
        assertEquals(0, progressListener.getPercentComplete(),
                "должно быть 0% после сброса");
    }

    // 9. Тест сброса из начального состояния.
    @Test
    void testResetFromInitialState() {
        // Сбрасываем без предварительных обновлений
        progressListener.reset();

        assertEquals(0, progressListener.getBytesRead(),
                " должно быть 0 после сброса");
        assertEquals(0, progressListener.getPercentComplete(),
                "должно быть 0% после сброса");
    }

     // 10. Тест минимального прогресса.
    @Test
    void testMinimalProgress() {
        progressListener.update(1, 100);
        assertEquals(1, progressListener.getPercentComplete(),
                "1 байт из 100 должен давать 1%");
    }


}