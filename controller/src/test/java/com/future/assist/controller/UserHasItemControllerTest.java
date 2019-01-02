package com.future.assist.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.future.assist.Assist;
import com.future.assist.ItemController;
import com.future.assist.UserHasItemController;
import com.future.assist.configuration.WebSecurityTestConfiguration;
import com.future.assist.mapper.UserHasItemMapper;
import com.future.assist.model.entity_model.Item;
import com.future.assist.model.entity_model.User;
import com.future.assist.model.entity_model.UserHasItem;
import com.future.assist.model.request_model.user.UserHasItemModelRequest;
import com.future.assist.model.response_model.ItemResponse;
import com.future.assist.model.response_model.PageResponse;
import com.future.assist.model.response_model.UserHasItemResponse;
import com.future.assist.model.response_model.UserResponse;
import com.future.assist.service.service_impl.LoggedinUserInfo;
import com.future.assist.service.service_interface.UserHasItemService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
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
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(UserHasItemController.class)
@Import(WebSecurityTestConfiguration.class)
@ContextConfiguration(classes = Assist.class)
public class UserHasItemControllerTest {

    @MockBean
    private UserHasItemService userHasItemService;

    @MockBean
    private LoggedinUserInfo loggedinUserInfo;

    @MockBean
    private UserHasItemMapper mapper;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    private UserHasItemResponse response;
    private UserHasItemModelRequest request;
    private UserHasItem userHasItem;
    private Item item;
    private User user;
    private User admin;
    private User nonadmin;
    private PageResponse<UserHasItemResponse> pageResponse;
    private ItemResponse itemResponse;
    private UserResponse userResponse;


    @Before
    public void setUp() throws Exception {
        user = new User();
        user.setIdUser(1L);
        user.setIsAdmin(false);
        user.setName("priagungs");

        admin = new User();
        admin.setIsAdmin(true);
        admin.setIdUser(2L);
        admin.setName("admin");

        nonadmin = new User();
        nonadmin.setIsAdmin(false);
        nonadmin.setIdUser(3L);
        nonadmin.setName("nonadmin");

        item = new Item();
        item.setIdItem(1L);
        item.setItemName("item1");

        userHasItem = new UserHasItem();
        userHasItem.setHasQty(10);
        userHasItem.setIdUserHasItem(1L);
        userHasItem.setUser(user);
        userHasItem.setItem(item);

        itemResponse = new ItemResponse();
        itemResponse.setIdItem(userHasItem.getItem().getIdItem());
        itemResponse.setItemName(userHasItem.getItem().getItemName());
        userResponse = new UserResponse();
        userResponse.setIdUser(userHasItem.getUser().getIdUser());
        userResponse.setIsAdmin(userHasItem.getUser().getIsAdmin());
        userResponse.setName(userHasItem.getUser().getName());
        response = new UserHasItemResponse();
        response.setUser(userResponse);
        response.setItem(itemResponse);
        response.setIdUserHasItem(userHasItem.getIdUserHasItem());
        response.setHasQty(userHasItem.getHasQty());

        request = new UserHasItemModelRequest();
        request.setIdUserHasItem(userHasItem.getIdUserHasItem());

        pageResponse = new PageResponse<>();
        pageResponse.setContent(Arrays.asList(response));
        pageResponse.setSize(1);

    }

    @Test
    public void readUserHasItems() throws Exception {
        Page<UserHasItem> userHasItems = new PageImpl<>(Arrays.asList(userHasItem));
        when(userHasItemService.readAllUserHasItemsByIdUser(any(), any()))
                .thenReturn(userHasItems);
        when(userHasItemService.readAllUserHasItemsByIdItem(any(), any()))
                .thenReturn(userHasItems);
        when(userHasItemService.readAllUserHasItems(any()))
                .thenReturn(userHasItems);
        when(mapper.pageToPageResponse(userHasItems))
                .thenReturn(pageResponse);
        mvc
                .perform(get("/api/user-items")
                        .param("page", "0")
                        .param("limit", "1")
                        .param("sort", "idUserHasItem"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].idUserHasItem",
                        is(userHasItem.getIdUserHasItem().intValue())))
                .andExpect(jsonPath("$.size", is(pageResponse.getSize())));

        mvc
                .perform(get("/api/user-items")
                        .param("page", "0")
                        .param("limit", "1")
                        .param("sort", "idUserHasItem")
                        .param("idUser", "1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].idUserHasItem",
                        is(userHasItem.getIdUserHasItem().intValue())))
                .andExpect(jsonPath("$.size", is(pageResponse.getSize())));

        mvc
                .perform(get("/api/user-items")
                        .param("page", "0")
                        .param("limit", "1")
                        .param("sort", "idUserHasItem")
                        .param("idItem", "1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].idUserHasItem",
                        is(userHasItem.getIdUserHasItem().intValue())))
                .andExpect(jsonPath("$.size", is(pageResponse.getSize())));
    }

    @Test
    public void readUserHasItem() throws Exception {
        when(userHasItemService.readUserHasItemById(1L)).thenReturn(userHasItem);
        when(mapper.entityToResponse(userHasItem)).thenReturn(response);
        mvc
                .perform(get("/api/user-items/" + 1L))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idUserHasItem", is(userHasItem.getIdUserHasItem().intValue())));
    }

    @Test
    public void deleteUserHasItem() throws Exception {
        when(userHasItemService.readUserHasItemById(1L)).thenReturn(userHasItem);
        when(loggedinUserInfo.getUser()).thenReturn(nonadmin);
        mvc
                .perform(delete("/api/user-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isUnauthorized());

        when(loggedinUserInfo.getUser()).thenReturn(admin);
        when(userHasItemService.deleteUserHasItem(1L)).thenReturn(ResponseEntity.ok().build());
        mvc
                .perform(delete("/api/user-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk());
    }
}