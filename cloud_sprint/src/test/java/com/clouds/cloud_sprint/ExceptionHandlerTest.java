package com.clouds.cloud_sprint;

import com.clouds.cloud_sprint.GlobalExceptionHandler;
import com.clouds.cloud_sprint.controller.HomeController;
import com.clouds.cloud_sprint.services.FileService;
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

@WebMvcTest(HomeController.class)
@Import(GlobalExceptionHandler.class) // ← Добавлено!
class ExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FileService fileService;

    @Test
    @WithMockUser(username = "testuser")
    void testDownloadFile_NotFound() throws Exception {
        when(fileService.getFileById(999L)).thenThrow(new RuntimeException("File not found"));

        mockMvc.perform(get("/home/download/999"))
                .andExpect(status().is5xxServerError())
                .andExpect(content().string("File not found")); // ← Опционально
    }

    @Test
    @WithMockUser(username = "testuser")
    void testDownloadFile_FileNotReadable() throws Exception {
        when(fileService.getFileById(1L)).thenThrow(new RuntimeException("File is not readable"));

        mockMvc.perform(get("/home/download/1"))
                .andExpect(status().is5xxServerError())
                .andExpect(content().string("File is not readable"));
    }
}