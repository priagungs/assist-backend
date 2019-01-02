package com.future.assist.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.future.assist.Assist;
import com.future.assist.configuration.WebSecurityTestConfiguration;
import com.future.assist.mapper.TransactionMapper;
import com.future.assist.model.entity_model.Transaction;
import com.future.assist.model.entity_model.User;
import com.future.assist.model.request_model.transaction.TransactionCreateRequest;
import com.future.assist.model.request_model.transaction.TransactionModelRequest;
import com.future.assist.model.request_model.user.UserModelRequest;
import com.future.assist.model.response_model.PageResponse;
import com.future.assist.model.response_model.TransactionResponse;
import com.future.assist.model.response_model.UserResponse;
import com.future.assist.printer.PrinterService;
import com.future.assist.service.service_impl.LoggedinUserInfo;
import com.future.assist.service.service_interface.TransactionService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(TransactionController.class)
@Import(WebSecurityTestConfiguration.class)
@ContextConfiguration(classes = Assist.class)
public class TransactionControllerTest {
    @MockBean
    private TransactionService transactionService;

    @MockBean
    private LoggedinUserInfo loggedinUserInfo;

    @MockBean
    private PrinterService printerService;

    @MockBean
    private TransactionMapper transactionMapper;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    private Transaction transaction;
    private TransactionCreateRequest createRequest;
    private TransactionResponse response;
    private TransactionModelRequest deleteRequest;
    private PageResponse<TransactionResponse> pageResponse;
    private User admin;
    private User nonadmin;

    @Before
    public void setUp() throws Exception {

        admin = new User();
        admin.setIdUser(1L);
        admin.setIsAdmin(true);

        nonadmin = new User();
        nonadmin.setIdUser(2L);
        nonadmin.setIsAdmin(false);

        transaction = new Transaction();
        transaction.setAdmin(admin);
        transaction.setIdTransaction(1L);

        createRequest = new TransactionCreateRequest();
        UserModelRequest user = new UserModelRequest();
        user.setIdUser(1L);
        createRequest.setAdmin(user);

        UserResponse userResponse = new UserResponse();
        userResponse.setIdUser(1L);
        userResponse.setIsAdmin(true);
        response = new TransactionResponse();
        response.setAdmin(userResponse);
        response.setIdTransaction(transaction.getIdTransaction());

        deleteRequest = new TransactionModelRequest();
        deleteRequest.setIdTransaction(transaction.getIdTransaction());

        pageResponse = new PageResponse<>();
        pageResponse.setContent(Arrays.asList(response));
        pageResponse.setSize(1);
    }

    @Test
    public void createTransactions() throws Exception {
        when(loggedinUserInfo.getUser()).thenReturn(nonadmin);
        mvc
                .perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(createRequest)))
                .andDo(print())
                .andExpect(status().isUnauthorized());

        when(loggedinUserInfo.getUser()).thenReturn(admin);
        when(transactionMapper.transactionRequestToEntity(createRequest)).thenReturn(transaction);
        when(transactionService.createTransaction(transaction)).thenReturn(transaction);
        when(transactionService.readTransactionByIdTransaction(transaction.getIdTransaction())).thenReturn(transaction);
        when(transactionMapper.transactionEntityToResponse(transaction)).thenReturn(response);
        mvc
                .perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(createRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idTransaction", is(transaction.getIdTransaction().intValue())));
    }

    @Test
    public void readAllTransactions() throws Exception {
        when(loggedinUserInfo.getUser()).thenReturn(nonadmin);
        mvc
                .perform(get("/api/transactions")
                        .param("page", "0")
                        .param("limit", "1")
                        .param("sort", "idTransaction"))
                .andDo(print())
                .andExpect(status().isUnauthorized());

        Page<Transaction> transactionPage = new PageImpl<>(Arrays.asList(transaction));
        when(loggedinUserInfo.getUser()).thenReturn(admin);
        when(transactionService.readAllTransactions(any())).thenReturn(transactionPage);
        when(transactionMapper.pageToPageResponse(transactionPage)).thenReturn(pageResponse);
        mvc
                .perform(get("/api/transactions")
                        .param("page", "0")
                        .param("limit", "1")
                        .param("sort", "idTransaction"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].idTransaction", is(transaction.getIdTransaction().intValue())))
                .andExpect(jsonPath("$.size", is(pageResponse.getSize())));

        mvc
                .perform(get("/api/transactions")
                        .param("page", "0")
                        .param("limit", "1")
                        .param("sort", "transactionDate"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].idTransaction", is(transaction.getIdTransaction().intValue())))
                .andExpect(jsonPath("$.size", is(pageResponse.getSize())));
    }

    @Test
    public void readTransactionById() throws Exception {
        when(loggedinUserInfo.getUser()).thenReturn(nonadmin);
        mvc
                .perform(get("/api/transactions/" + transaction.getIdTransaction()))
                .andDo(print())
                .andExpect(status().isUnauthorized());

        when(loggedinUserInfo.getUser()).thenReturn(admin);
        when(transactionService.readTransactionByIdTransaction(transaction.getIdTransaction())).thenReturn(transaction);
        when(transactionMapper.transactionEntityToResponse(transaction)).thenReturn(response);
        mvc
                .perform(get("/api/transactions/" + transaction.getIdTransaction()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idTransaction", is(transaction.getIdTransaction().intValue())));
    }

    @Test
    public void deleteTransaction() throws Exception {
        when(loggedinUserInfo.getUser()).thenReturn(nonadmin);
        mvc
                .perform(delete("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(deleteRequest)))
                .andDo(print())
                .andExpect(status().isUnauthorized());

        when(loggedinUserInfo.getUser()).thenReturn(admin);
        when(transactionService.deleteTransaction(transaction.getIdTransaction()))
                .thenReturn(ResponseEntity.ok().build());
        mvc
                .perform(delete("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(deleteRequest)))
                .andDo(print())
                .andExpect(status().isOk());
    }
}