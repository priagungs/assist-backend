package com.future.assist.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.future.assist.Assist;
import com.future.assist.configuration.WebSecurityTestConfiguration;
import com.future.assist.exception.InvalidValueException;
import com.future.assist.mapper.ReqMapper;
import com.future.assist.model.RequestStatus;
import com.future.assist.model.entity_model.Item;
import com.future.assist.model.entity_model.Request;
import com.future.assist.model.entity_model.User;
import com.future.assist.model.request_model.item.ItemCreateUpdateRequest;
import com.future.assist.model.request_model.request.ReqCreateRequest;
import com.future.assist.model.request_model.request.ReqItemCreateRequest;
import com.future.assist.model.request_model.request.ReqModelRequest;
import com.future.assist.model.request_model.request.ReqUpdateRequest;
import com.future.assist.model.response_model.*;
import com.future.assist.service.service_impl.LoggedinUserInfo;
import com.future.assist.service.service_interface.RequestService;
import com.future.assist.service.service_interface.UserService;
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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(RequestController.class)
@Import(WebSecurityTestConfiguration.class)
@ContextConfiguration(classes = Assist.class)
public class RequestControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private RequestService requestService;

    @MockBean
    private ReqMapper reqMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private LoggedinUserInfo userInfo;

    private User superior;
    private User user;
    private User admin;
    private User nonadmin;
    private Page<User> users;
    private PageResponse<ReqResponse> pageResponse;
    private ReqResponse response;
    private Item item;
    private UserResponse userResponse;
    private SuperiorResponse superiorResponse;
    private ItemResponse itemResponse;
    private Request request;
    private Page<Request> requests;
    private ReqCreateRequest createRequest;
    private ReqItemCreateRequest reqItemCreateRequest;
    private List<ReqItemCreateRequest> listReqItemCreateRequest;
    private ReqModelRequest deleteRequest;
    private ReqUpdateRequest updateRequest;


    @Before
    public void setUp() {
        item = new Item();
        item.setDescription("test123");
        item.setItemName("item1");
        item.setPictureURL("img.jpg");
        item.setPrice(1000L);
        item.setTotalQty(100);
        item.setIdItem(1L);

        superior = new User();
        superior.setIdUser(0L);
        superior.setIsActive(true);

        user = new User();
        user.setIdUser(1L);
        user.setIsActive(true);
        user.setIsAdmin(true);
        user.setUsername("dummy");
        user.setName("Dummy User");
        user.setPassword("pass");
        user.setPictureURL("photo.png");
        user.setRole("Programmer");
        user.setDivision("Technology");
        user.setSuperior(superior);

        admin = new User();
        admin.setIdUser(8L);
        admin.setIsAdmin(true);

        nonadmin = new User();
        nonadmin.setIsAdmin(false);

        users = new PageImpl<>(Arrays.asList(user));

        request = new Request();
        request.setRequestBy(user);
        request.setItem(item);
        request.setReqQty(2);
        request.setRequestStatus(RequestStatus.REQUESTED);

        requests = new PageImpl<>(Arrays.asList(request));

        superiorResponse = new SuperiorResponse();
        superiorResponse.setIdUser(0L);
        superiorResponse.setIsAdmin(true);
        superiorResponse.setIsActive(true);
        superiorResponse.setUsername("superior");
        superiorResponse.setName("Superior Name");
        superiorResponse.setRole("Programmer");
        superiorResponse.setDivision("Technology");
        superiorResponse.setPictureURL("photo.png");

        userResponse = new UserResponse();
        userResponse.setIdUser(1L);
        userResponse.setIsAdmin(true);
        userResponse.setUsername("dummy");
        userResponse.setName("Dummy User");
        userResponse.setRole("Programmer");
        userResponse.setDivision("Technology");
        userResponse.setPictureURL("photo.png");
        userResponse.setIsActive(true);
        userResponse.setSuperior(superiorResponse);

        itemResponse = new ItemResponse();
        itemResponse.setAvailableQty(100);
        itemResponse.setDescription("test123");
        itemResponse.setIdItem(1L);
        itemResponse.setIsActive(true);
        itemResponse.setItemName("item1");
        itemResponse.setPictureURL("img.jpg");
        itemResponse.setPrice(1000L);
        itemResponse.setTotalQty(100);

        response = new ReqResponse();
        response.setIdRequest(1L);
        response.setRequestBy(userResponse);
        response.setItem(itemResponse);
        response.setReqQty(1);
        response.setRequestStatus(RequestStatus.REQUESTED);
        response.setApprovedBy(superiorResponse.getIdUser());
        response.setRejectedBy(superiorResponse.getIdUser());
        response.setHandedOverBy(admin.getIdUser());
        response.setReturnedBy(user.getIdUser());


        pageResponse = new PageResponse<>();
        pageResponse.setContent(Arrays.asList(response));
        pageResponse.setTotalPages(1);
        pageResponse.setTotalElements(1L);
        pageResponse.setLast(true);
        pageResponse.setNumberOfElements(1);
        pageResponse.setFirst(true);
        pageResponse.setSize(1);
        pageResponse.setNumber(1);

        reqItemCreateRequest = new ReqItemCreateRequest();
        reqItemCreateRequest.setItem(item);
        reqItemCreateRequest.setRequestQty(2);

        listReqItemCreateRequest = new ArrayList<>();
        listReqItemCreateRequest.add(reqItemCreateRequest);

        createRequest = new ReqCreateRequest();
        createRequest.setItems(listReqItemCreateRequest);
        createRequest.setIdUser(user.getIdUser());

        deleteRequest = new ReqModelRequest();
        deleteRequest.setIdRequest(user.getIdUser());

        updateRequest = new ReqUpdateRequest();
        updateRequest.setIdAdmin(admin.getIdUser());
        updateRequest.setIdSuperior(superior.getIdUser());
        updateRequest.setIdRequest(request.getIdRequest());
        updateRequest.setRequestStatus(RequestStatus.APPROVED);
    }

    @Test
    public void createRequestsSuccessTest() throws Exception {
        List<Request> reqs = new ArrayList<>();
        reqs.add(request);
        when(requestService.createRequest(any()))
                .thenReturn(reqs);
        when(reqMapper.entityToResponse(request))
                .thenReturn(response);

        mvc
                .perform(post("/api/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(createRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].idRequest", is(response.getIdRequest().intValue())));
    }

    @Test
    public void readRequestsSuccessTest() throws Exception {
        when(requestService.readAllRequest(any()))
                .thenReturn(requests);
        when(reqMapper.pageToPageResponse(requests))
                .thenReturn(pageResponse);

        mvc
                .perform(get("/api/requests")
                        .param("page", "0")
                        .param("limit", "1")
                        .param("sort", "idUser")
                        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].idRequest", is(response.getIdRequest().intValue())))
                .andExpect(jsonPath("$.totalPages", is(pageResponse.getTotalPages())));
    }

    @Test
    public void readRequestsSuccessTestWithIdUserAndStatus() throws Exception {

        when(requestService.readAllRequestByUserAndStatus(any(),any(),any()))
                .thenReturn(requests);
        when(reqMapper.pageToPageResponse(requests))
                .thenReturn(pageResponse);

        mvc
                .perform(get("/api/requests")
                        .param("page", "0")
                        .param("limit", "1")
                        .param("idUser","1")
                        .param("status","REQUESTED")
                        .param("sort", "idUser")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].idRequest", is(response.getIdRequest().intValue())))
                .andExpect(jsonPath("$.totalPages", is(pageResponse.getTotalPages())));
    }

    @Test
    public void readRequestsSuccessTestWithIdUser() throws Exception {

        when(requestService.readRequestByUser(any(),any()))
                .thenReturn(requests);
        when(reqMapper.pageToPageResponse(requests))
                .thenReturn(pageResponse);

        mvc
                .perform(get("/api/requests")
                        .param("page", "0")
                        .param("limit", "1")
                        .param("idUser","1")
                        .param("sort", "idUser")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].idRequest", is(response.getIdRequest().intValue())))
                .andExpect(jsonPath("$.totalPages", is(pageResponse.getTotalPages())));
    }

    @Test
    public void readRequestsSuccessTestWithIdSuperiorAndRequestStatus() throws Exception {
        when(requestService.readAllRequestBySuperiorAndRequestStatus(any(), any(), any()))
                .thenReturn(requests);
        when(reqMapper.pageToPageResponse(requests))
                .thenReturn(pageResponse);

        mvc
                .perform(get("/api/requests")
                        .param("page", "0")
                        .param("limit", "1")
                        .param("idSuperior", "1")
                        .param("sort", "idUser")
                        .param("status", "REQUESTED")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].idRequest", is(response.getIdRequest().intValue())))
                .andExpect(jsonPath("$.totalPages", is(pageResponse.getTotalPages())));
    }

    @Test
    public void readRequestsSuccessTestWithIdSuperior() throws Exception {
        when(requestService.readAllRequestBySuperior(any(), any()))
                .thenReturn(requests);
        when(reqMapper.pageToPageResponse(requests))
                .thenReturn(pageResponse);

        mvc
                .perform(get("/api/requests")
                        .param("page", "0")
                        .param("limit", "1")
                        .param("idSuperior", "1")
                        .param("sort", "idUser")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].idRequest", is(response.getIdRequest().intValue())))
                .andExpect(jsonPath("$.totalPages", is(pageResponse.getTotalPages())));
    }

    @Test
    public void readRequestsSuccessTestWithIStatus() throws Exception {
        when(requestService.readAllRequestByRequestStatus(any(), any()))
                .thenReturn(requests);
        when(reqMapper.pageToPageResponse(requests))
                .thenReturn(pageResponse);

        mvc
                .perform(get("/api/requests")
                        .param("page", "0")
                        .param("limit", "1")
                        .param("status", "REQUESTED")
                        .param("sort", "idUser")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].idRequest", is(response.getIdRequest().intValue())))
                .andExpect(jsonPath("$.totalPages", is(pageResponse.getTotalPages())));
    }

    @Test
    public void updateRequestUnauthorized1Test() throws Exception{
        when(userInfo.getUser())
                .thenReturn(admin);
        mvc
                .perform(put("/api/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(Arrays.asList(updateRequest)))
                        .accept(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void updateRequestUnauthorized2Test() throws Exception{
        when(userInfo.getUser())
                .thenReturn(nonadmin);
        updateRequest.setRequestStatus(RequestStatus.SENT);
        mvc
                .perform(put("/api/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(Arrays.asList(updateRequest)))
                        .accept(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void updateRequestInvalidValueTest() throws Exception{
        updateRequest.setRequestStatus(RequestStatus.REQUESTED);
        mvc
                .perform(put("/api/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(Arrays.asList(updateRequest)))
                        .accept(MediaType.APPLICATION_JSON)).andDo(print())
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateRequestSuccessApprovedOrRejectedTest() throws Exception{
        when(userInfo.getUser())
                .thenReturn(superior);
        when(reqMapper.entityToResponse(any()))
                .thenReturn(response);
        mvc
                .perform(put("/api/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(Arrays.asList(updateRequest)))
                        .accept(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void updateRequestSuccessSentTest() throws Exception{
        updateRequest.setRequestStatus(RequestStatus.SENT);
        when(userInfo.getUser())
                .thenReturn(admin);
        when(reqMapper.entityToResponse(any()))
                .thenReturn(response);
        mvc
                .perform(put("/api/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(Arrays.asList(updateRequest)))
                        .accept(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk());
    }


    @Test
    public void deleteRequestSuccess() throws Exception{

        when(requestService.readRequestByIdRequest(any()))
                .thenReturn(request);

        when(userInfo.getUser())
                .thenReturn(admin);
        mvc
                .perform(delete("/api/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(deleteRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void deleteRequestUnAuthorizedTest() throws Exception{

        when(requestService.readRequestByIdRequest(any()))
                .thenReturn(request);

        when(userInfo.getUser())
                .thenReturn(request.getRequestBy());
        mvc
                .perform(delete("/api/requests")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(deleteRequest))
                    .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }
}