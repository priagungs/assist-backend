package com.future.assist.service.service_impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ResourceBanner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.MalformedURLException;
import java.nio.file.Paths;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
@SpringBootTest
public class BackupRestoreServiceTest {

    @MockBean
    FileStorageService fileStorageService;

    @Autowired
    BackupRestoreService backupRestoreService;

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void backup() throws Exception {
        when(fileStorageService.loadFileAsResource(any()))
                .thenReturn(new UrlResource(Paths.get("./service/src/test/java/com/future/assist/service/service_impl/restoretest").toUri()));
        assertEquals(HttpStatus.OK, backupRestoreService.backup().getStatusCode());
    }

    @Test
    public void restore() {
        when(fileStorageService.storeFile(any()))
                .thenReturn("abcdef");
        MockMultipartFile file = new MockMultipartFile("file", "test.txt",
                "text/plain", "some xml".getBytes());
        assertEquals(HttpStatus.EXPECTATION_FAILED, backupRestoreService.restore(file).getStatusCode());
    }
}