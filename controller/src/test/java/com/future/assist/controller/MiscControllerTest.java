package com.future.assist.controller;

import com.future.assist.Assist;
import com.future.assist.configuration.WebSecurityTestConfiguration;
import com.future.assist.mapper.UserMapper;
import com.future.assist.model.entity_model.Item;
import com.future.assist.model.entity_model.Transaction;
import com.future.assist.model.entity_model.User;
import com.future.assist.model.response_model.UserResponse;
import com.future.assist.printer.PrinterService;
import com.future.assist.service.service_impl.BackupRestoreService;
import com.future.assist.service.service_impl.FileStorageService;
import com.future.assist.service.service_impl.LoggedinUserInfo;
import com.future.assist.service.service_interface.ItemService;
import com.future.assist.service.service_interface.TransactionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Paths;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(MiscController.class)
@Import(WebSecurityTestConfiguration.class)
@ContextConfiguration(classes = Assist.class)
public class MiscControllerTest {
    @MockBean
    private FileStorageService storageService;

    @MockBean
    private LoggedinUserInfo loggedinUserInfo;

    @MockBean
    private TransactionService transactionService;

    @MockBean
    private PrinterService printerService;

    @MockBean
    private ItemService itemService;

    @MockBean
    private UserMapper mapper;

    @MockBean
    private BackupRestoreService backupRestoreService;

    @Autowired
    private MockMvc mvc;

    @Test
    public void getLoginDetail() throws Exception {
        User user = new User();
        user.setIdUser(1L);
        UserResponse userResponse = new UserResponse();
        userResponse.setIdUser(user.getIdUser());

        when(loggedinUserInfo.getUser()).thenReturn(user);
        when(mapper.entityToResponse(user)).thenReturn(userResponse);
        mvc
                .perform(get("/api/login-detail"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idUser", is(user.getIdUser().intValue())));
    }

    @Test
    public void generateItemDetail() throws Exception {
        Item item = new Item();
        item.setIdItem(2L);
        when(itemService.readItemByIdItem(item.getIdItem())).thenReturn(item);
        doNothing().when(printerService).printItem(item);
        when(storageService.loadFileAsResource(any())).thenReturn(new UrlResource(Paths
                .get("./controller/test/java/com/future/assist/controller/testfile").toUri()));
        mvc
                .perform(get("/api/item-detail/" + item.getIdItem()))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void generateInvoice() throws Exception {
        Transaction transaction = new Transaction();
        transaction.setIdTransaction(1L);
        when(transactionService.readTransactionByIdTransaction(transaction.getIdTransaction())).thenReturn(transaction);
        doNothing().when(printerService).printInvoice(transaction);
        when(storageService.loadFileAsResource(any())).thenReturn(new UrlResource(Paths
                .get("./controller/test/java/com/future/assist/controller/testfile").toUri()));
        mvc
                .perform(get("/api/invoice/" + transaction.getIdTransaction()))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void backup() throws Exception {
        User user = new User();
        user.setIsAdmin(false);
        when(loggedinUserInfo.getUser()).thenReturn(user);
        mvc
                .perform(get("/api/backup"))
                .andDo(print())
                .andExpect(status().isUnauthorized());

        user.setIsAdmin(true);
        when(backupRestoreService.backup()).thenReturn(ResponseEntity.ok().build());
        mvc
                .perform(get("/api/backup"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void restore() throws Exception {
        User user = new User();
        user.setIsAdmin(false);
        when(loggedinUserInfo.getUser()).thenReturn(user);
        MockMultipartFile file = new MockMultipartFile("file", "test.txt",
                "text/plain", "some xml".getBytes());
        mvc
                .perform(multipart("/api/restore")
                        .file(file))
                .andDo(print())
                .andExpect(status().isUnauthorized());

        when(backupRestoreService.restore(file)).thenReturn(ResponseEntity.ok().build());
        user.setIsAdmin(true);
        mvc
                .perform(multipart("/api/restore")
                        .file(file))
                .andDo(print())
                .andExpect(status().isOk());
    }
}