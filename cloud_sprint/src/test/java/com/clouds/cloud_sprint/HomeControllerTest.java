package com.clouds.cloud_sprint;

import com.clouds.cloud_sprint.model.Users;
import com.clouds.cloud_sprint.services.FileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.clouds.cloud_sprint.security.SecurityConfig;

import com.clouds.cloud_sprint.model.File;
import com.clouds.cloud_sprint.controller.HomeController;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;

import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(HomeController.class)
@Import(SecurityConfig.class)
class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FileService fileService;

    private Users testUser;

    @BeforeEach
    void setUp() {
        testUser = new Users();
        testUser.setUsername("testuser");

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(
                new UsernamePasswordAuthenticationToken(testUser, null, testUser.getAuthorities())
        );
        SecurityContextHolder.setContext(securityContext);
    }

    //  Загрузка главной страницы
    @Test
    void homePage() throws Exception {
        // *условия выполнения*
        when(fileService.getFilesByUser(any())).thenReturn(Collections.emptyList());

        // *ожидаемые результаты*
        // GET запрос
        mockMvc.perform(get("/home"))
                .andExpect(status().isOk())     //200
                .andExpect(view().name("home"))
                .andExpect(model().attributeExists("files")); //появление атрибута
    }

    //  Загрузка нового непустого файла
    @Test
    void uploadValidFile() throws Exception {

        MockMultipartFile file = new MockMultipartFile(
                "file", "test.txt", "text/plain", "content".getBytes()
        );

        // передаваемый сервису файл
        File savedFile = new File();
        savedFile.setId(1L);
        savedFile.setFileName("test.txt");

        // *условия выполнения*
        when(fileService.addFile(any(), any()))
                .thenReturn(CompletableFuture.completedFuture(savedFile));

        // *ожидаемые результаты*
        // POST-запрос (для загрузки файлов)
        mockMvc.perform(multipart("/home/upload")
                        .file(file)
                        .with(csrf())) //токен безопасности, иначе 403
                .andExpect(status().is3xxRedirection()) //перенаправление на /home
                .andExpect(redirectedUrl("/home"));
    }

    //  Загрузка пустого файла
    @Test
    void uploadEmptyFile() throws Exception {
        MockMultipartFile emptyFile = new MockMultipartFile(
                "file", "empty.txt", "text/plain", new byte[0]
        );

        // *условия выполнения*
        when(fileService.addFile(any(), any()))
                .thenReturn(CompletableFuture.failedFuture(new IllegalArgumentException("Empty file")));

        // *ожидаемые результаты*
        // проверяем что нет ошибки 500
        mockMvc.perform(multipart("/home/upload")
                        .file(emptyFile)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));
    }

    // Скачивание существующего файла
    @Test
    void downloadFile() throws Exception {

        // Создание тестового объекта-файла
        File file = new File();
        file.setFileName("test.txt");
        file.setFilePath("/directory"); // Полный путь
        file.setContentType("text/plain");
        file.setFileSize(100L);

        // Создание реального временного файла
        Path tempFile = Files.createTempFile("test", ".txt");
        Files.write(tempFile, "test content".getBytes());
        file.setFilePath(tempFile.toString());

        // *условия выполнения*
        when(fileService.getFileById(1L)).thenReturn(file);

        mockMvc.perform(get("/home/download/1"))
                .andExpect(status().isOk())      // 200
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION,
                        containsString("attachment")))               // нужно скачать
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION,
                        containsString("filename=\"test.txt\"")));

        // Удаление временного файл
        Files.deleteIfExists(tempFile);
    }


    // Cкачивание несуществующего файла
    @Test
    void downloadNoFile() throws Exception {
        Long fileId = 99L;

        // Мокируем сервис, чтобы он выбросил исключение
        when(fileService.getFileById(fileId)).thenThrow(new RuntimeException("File not found"));

        mockMvc.perform(get("/home/download/{id}", fileId))
                .andExpect(status().isInternalServerError())  // 500, так как срабатывает GlobalExceptionHandler
                .andExpect(content().string("File not found"));
    }

    // Удаления файла
    @Test
    void deleteFile() throws Exception {
        Long fileId = 1L;

        when(fileService.deleteFile(fileId))
                .thenReturn(CompletableFuture.completedFuture(null));

        // *ожидаемые результаты*
        // POST-запрос на удаление
        mockMvc.perform(post("/home/delete/1")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection()) //перенаправление на /home
                .andExpect(redirectedUrl("/home"));
    }

    //  Удаления несуществующего файла
    @Test
    void DeleteNoFile() throws Exception {
        Long FileId = 99L;
        // Мокируем исключение при удалении несуществующего файла
        doThrow(new RuntimeException("File not found."))
                .when(fileService).deleteFile(FileId);

        mockMvc.perform(post("/home/delete/{id}", FileId)
                        .with(csrf()))
                .andExpect(status().isInternalServerError())  // 500, так как срабатывает GlobalExceptionHandler
                .andExpect(content().string("File not found."));

        // Проверяем запуск сервиса
        verify(fileService).deleteFile(FileId);
    }


    // список файлов
    @Test
    void fileList() throws Exception {

        File file1 = new File();
        file1.setFileName("file1.txt");
        file1.setFileSize(100L);

        File file2 = new File();
        file2.setFileName("file2.txt");
        file2.setFileSize(100L);

        File file3 = new File();
        file3.setFileName("file3.txt");
        file3.setFileSize(100L);

        List<File> files = Arrays.asList(file1, file2, file3);

        when(fileService.getFilesByUser(any())).thenReturn(files);

        mockMvc.perform(get("/home"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("files", files));
    }

    // Загрузка без CSRF
    @Test
    void uploadWithoutCsrf() throws Exception {

        MockMultipartFile file = new MockMultipartFile(
                "file", "test.txt", "text/plain", "content".getBytes()
        );

        mockMvc.perform(multipart("/home/upload").file(file))
                .andExpect(status().isForbidden()); //403
    }

    // Удаление без CSRF
    @Test
    void deleteWithoutCsrf() throws Exception {
        mockMvc.perform(post("/home/delete/1"))
                .andExpect(status().isForbidden());  //403
    }

    // Доступ к домашней странице без аутентификации
    @Test
    void homePageWithoutAuthentication() throws Exception {
        // симуляция неаутентифицированного пользователя
        SecurityContextHolder.clearContext();

        mockMvc.perform(get("/home"))
                .andExpect(status().is3xxRedirection()) // Должен перенаправить на логин
                .andExpect(redirectedUrlPattern("**/login"));
    }

    // Загрузка файла с ошибкой (напр. нет места)
    @Test
    void uploadFileWithIOException() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file", "large.txt", "text/plain", "large content".getBytes()
        );

        // Симулируем IOException при сохранении файла
        when(fileService.addFile(any(), any()))
                .thenReturn(CompletableFuture.failedFuture(new IOException("Disk full")));

        mockMvc.perform(multipart("/home/upload")
                        .file(file)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));
    }

    // Получение пустого списка файлов
    @Test
    void emptyFileList() throws Exception {

        when(fileService.getFilesByUser(any())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/home"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(model().attribute("files", Collections.emptyList())) // Пустой список
                .andExpect(model().attributeDoesNotExist("error")); // Нет ошибок
    }

}
