package com.future.assist.service.service_impl;

import com.future.assist.model.entity_model.Item;
import com.future.assist.model.entity_model.Request;
import com.future.assist.model.entity_model.User;
import com.future.assist.model.request_model.request.ReqCreateRequest;
import com.future.assist.model.request_model.request.ReqUpdateRequest;
import com.future.assist.repository.RequestRepository;
import com.future.assist.service.service_interface.ItemService;
import com.future.assist.service.service_interface.RequestService;
import com.future.assist.service.service_interface.UserHasItemService;
import com.future.assist.service.service_interface.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RequestServiceImplTest {

    @MockBean
    private RequestRepository requestRepository;

    @Autowired
    private RequestService requestService;

    @MockBean
    private UserService userService;

    @MockBean
    private ItemService itemService;

    @MockBean
    private UserHasItemService userHasItemService;

    private User user;
    private User user2;
    private Item item1;
    private Item item2;
    private Request request;
    private Request request2;
    private ReqUpdateRequest requestUpdate;
    private ReqCreateRequest requestBody;

    @Before

    @Test
    public void createRequest() {
    }

    @Test
    public void updateRequest() {
    }

    @Test
    public void updateRequestByRequestObject() {
    }

    @Test
    public void updateRequestStatusToReturned() {
    }

    @Test
    public void readAllRequestsByItem() {
    }

    @Test
    public void readAllRequest() {
    }

    @Test
    public void readRequestByUser() {
    }

    @Test
    public void readAllRequestBySuperior() {
    }

    @Test
    public void readAllRequestByRequestStatus() {
    }

    @Test
    public void readAllRequestBySuperiorAndRequestStatus() {
    }

    @Test
    public void readAllRequestByUserAndStatus() {
    }

    @Test
    public void deleteRequest() {
    }

    @Test
    public void readRequestByIdRequest() {
    }
}