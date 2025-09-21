package com.clouds.cloud_sprint;

import com.clouds.cloud_sprint.model.File;
import com.clouds.cloud_sprint.model.FileUploadProgressListener;
import com.clouds.cloud_sprint.model.Users;
import com.clouds.cloud_sprint.services.FileService;
// JUnit 5
import io.qameta.allure.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
// Mockito
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
// Spring для MockMultipartFile
import org.springframework.mock.web.MockMultipartFile;
// Java
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
// AssertJ для assertions
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
// Mockito для верификации
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Epic("Файловые операции")
@Feature("Граничные случаи работы с файлами")
@Story("Тестирование граничных случаев при работе с файлами")
@Owner("Команда разработки CloudSprint")
@Severity(SeverityLevel.CRITICAL)
@ExtendWith(MockitoExtension.class)
class FileServiceEdgeCasesTest {
    @Mock
    FileRepository fileRepository;
    @Mock
    FileUploadProgressListener progressListener;
    @InjectMocks
    FileService fileService;

    private Users user;

    @BeforeEach
    void setUp() {
        user = new Users();
        user.setId(1L);
        user.setUsername("john");
        user.setBaseFolderPath("/home/john");
    }

    @Test
    @Description("Тест проверяет корректную обработку очень больших файлов (1MB). " +
            "Используется техника тест-дизайна 'Граничные значения' для проверки работы системы " +
            "с файлами максимального размера. Тест важен для обеспечения корректной работы системы " +
            "при работе с большими файлами и предотвращения переполнения буферов.")
    @AllureId("FILE-EDGE-001")
    @Step("Проверка загрузки большого файла")
    void addFile_HandlesVeryLargeFile() throws IOException {
        Allure.step("Подготовка данных: создание большого файла размером 1MB");
        byte[] largeContent = new byte[1024 * 1024]; // 1MB
        MockMultipartFile largeFile = new MockMultipartFile("file", "large.bin", "application/octet-stream", largeContent);
        when(fileRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Allure.step("Запуск загрузки большого файла");
        CompletableFuture<File> future = fileService.addFile(largeFile, user);

        Allure.step("Проверка результатов загрузки");
        assertThat(future).isCompleted();
        verify(progressListener, atLeastOnce()).update(anyLong(), eq(1048576L));
    }

    @Test
    @Description("Тест проверяет корректную обработку специальных символов в имени файла. " +
            "Используется техника тест-дизайна 'Классы эквивалентности' для проверки работы системы " +
            "с различными типами символов (кириллица, пробелы, специальные знаки). Тест важен для " +
            "обеспечения корректной работы системы с файлами, содержащими различные символы в имени.")
    @AllureId("FILE-EDGE-002")
    @Step("Проверка обработки специальных символов в имени файла")
    void addFile_HandlesSpecialCharactersInFileName() throws IOException {
        Allure.step("Подготовка данных: создание файла со специальными символами в имени");
        MockMultipartFile file = new MockMultipartFile("file", "файл с пробелами и !@#.txt", "text/plain", "content".getBytes());
        when(fileRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Allure.step("Запуск загрузки файла со специальными символами");
        CompletableFuture<File> future = fileService.addFile(file, user);

        File saved = future.join();
        Allure.step("Проверка имени файла");
        assertThat(saved.getFileName()).isEqualTo("файл с пробелами и !@#.txt");
    }

    @Test
    @Description("Тест проверяет обработку null ID при удалении файла. " +
            "Используется техника тест-дизайна 'Граничные значения' для проверки работы системы " +
            "с некорректными входными данными (null вместо ID). Тест важен для обеспечения корректной " +
            "обработки ошибок при передаче некорректных данных.")
    @AllureId("FILE-EDGE-003")
    @Step("Проверка обработки null ID при удалении файла")
    void deleteFile_HandlesNullId() {
        Allure.step("Попытка удаления файла с null ID");
        assertThatThrownBy(() -> fileService.deleteFile(null))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @Description("Тест проверяет обработку null ID при получении файла по ID. " +
            "Используется техника тест-дизайна 'Граничные значения' для проверки работы системы " +
            "с некорректными входными данными (null вместо ID). Тест важен для обеспечения корректной " +
            "обработки ошибок при передаче некорректных данных.")
    @AllureId("FILE-EDGE-004")
    @Step("Проверка обработки null ID при получении файла по ID")
    void getFileById_HandlesNullId() {
        Allure.step("Попытка получения файла с null ID");
        assertThatThrownBy(() -> fileService.getFileById(null))
                .isInstanceOf(RuntimeException.class);
    }

}
