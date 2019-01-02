package com.future.assist.controller;

import com.future.assist.Assist;
import com.future.assist.configuration.WebSecurityTestConfiguration;
import com.future.assist.service.service_impl.FileStorageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(FileController.class)
@Import(WebSecurityTestConfiguration.class)
@ContextConfiguration(classes = Assist.class)
public class FileControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FileStorageService storageService;

    @Test
    public void uploadFile() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.txt",
                "text/plain", "some xml".getBytes());
        when(storageService.storeFile(file)).thenReturn("filename.txt");
        mockMvc
                .perform(multipart("/api/upload")
                        .file(file))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.file", is("http://localhost/api/download/filename.txt")));
    }

    @Test
    public void downloadFile() throws Exception {
        mockMvc
                .perform(get("/api/download/filename.txt"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}