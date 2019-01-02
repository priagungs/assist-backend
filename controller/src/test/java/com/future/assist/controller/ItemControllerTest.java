package com.future.assist.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.future.assist.Assist;
import com.future.assist.ItemController;
import com.future.assist.configuration.WebSecurityTestConfiguration;
import com.future.assist.mapper.ItemMapper;
import com.future.assist.model.entity_model.Item;
import com.future.assist.model.entity_model.User;
import com.future.assist.model.request_model.item.ItemCreateUpdateRequest;
import com.future.assist.model.request_model.item.ItemModelRequest;
import com.future.assist.model.response_model.ItemResponse;
import com.future.assist.model.response_model.PageResponse;
import com.future.assist.service.service_impl.LoggedinUserInfo;
import com.future.assist.service.service_interface.ItemService;
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

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ItemController.class)
@Import(WebSecurityTestConfiguration.class)
@ContextConfiguration(classes = Assist.class)
public class ItemControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private ItemService itemService;

    @MockBean
    private ItemMapper itemMapper;

    @MockBean
    private LoggedinUserInfo loggedinUserInfo;

    private ItemResponse response;
    private ItemCreateUpdateRequest request;
    private PageResponse<ItemResponse> pageResponse;
    private ItemModelRequest deleteRequest;
    private Item item;
    private User admin;
    private User nonadmin;

    @Before
    public void setUp() {

        request = new ItemCreateUpdateRequest();
        request.setDescription("test123");
        request.setItemName("item1");
        request.setPictureURL("img.jpg");
        request.setPrice(1000L);
        request.setTotalQty(100);

        item = new Item();
        item.setDescription("test123");
        item.setItemName("item1");
        item.setPictureURL("img.jpg");
        item.setPrice(1000L);
        item.setTotalQty(100);
        item.setIdItem(1L);

        response = new ItemResponse();
        response.setAvailableQty(100);
        response.setDescription("test123");
        response.setIdItem(1L);
        response.setIsActive(true);
        response.setItemName("item1");
        response.setPictureURL("img.jpg");
        response.setPrice(1000L);
        response.setTotalQty(100);

        pageResponse = new PageResponse<>();
        pageResponse.setContent(Arrays.asList(response));
        pageResponse.setFirst(true);
        pageResponse.setLast(true);
        pageResponse.setNumber(1);
        pageResponse.setNumberOfElements(1);
        pageResponse.setSize(1);
        pageResponse.setTotalElements(1L);
        pageResponse.setTotalPages(1);

        deleteRequest = new ItemModelRequest();
        deleteRequest.setIdItem(1L);

        admin = new User();
        admin.setIsAdmin(true);

        nonadmin = new User();
        nonadmin.setIsAdmin(false);

    }

    @Test
    public void createItem() throws Exception {
        when(loggedinUserInfo.getUser()).thenReturn(nonadmin);
        mvc
                .perform(post("/api/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(Arrays.asList(request)))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());

        when(loggedinUserInfo.getUser()).thenReturn(admin);
        when(itemMapper.requestToEntity(request)).thenReturn(item);
        when(itemService.createItem(item)).thenReturn(item);
        when(itemMapper.entityToResponse(item)).thenReturn(response);

        mvc
                .perform(post("/api/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(Arrays.asList(request)))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].idItem", is(response.getIdItem().intValue())));
    }

    @Test
    public void readAllItems() throws Exception {
        Page<Item> items = new PageImpl<>(Arrays.asList(item));
        when(itemService.readAllItems(any())).thenReturn(items);
        when(itemService.readAllItemsByKeywordAndAvailableGreaterThan(any(), any(), any())).thenReturn(items);
        when(itemService.readAllItemsContaining(any(), any())).thenReturn(items);
        when(itemService.readItemsByAvailableGreaterThan(any(), any())).thenReturn(items);
        when(itemMapper.pageToPageResponse(items)).thenReturn(pageResponse);

        when(loggedinUserInfo.getUser()).thenReturn(admin);
        mvc
                .perform(get("/api/items")
                        .param("page", "0")
                        .param("limit", "1")
                        .param("sort", "idItem"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].idItem", is(response.getIdItem().intValue())))
                .andExpect(jsonPath("$.totalPages", is(pageResponse.getTotalPages())));

        mvc
                .perform(get("/api/items")
                        .param("page", "0")
                        .param("limit", "1")
                        .param("sort", "idItem")
                        .param("keyword", "item"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].idItem", is(response.getIdItem().intValue())))
                .andExpect(jsonPath("$.totalPages", is(pageResponse.getTotalPages())));

        mvc
                .perform(get("/api/items")
                        .param("page", "0")
                        .param("limit", "1")
                        .param("sort", "idItem")
                        .param("keyword", "item")
                        .param("minqty", "50"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].idItem", is(response.getIdItem().intValue())))
                .andExpect(jsonPath("$.totalPages", is(pageResponse.getTotalPages())));

        mvc
                .perform(get("/api/items")
                        .param("page", "0")
                        .param("limit", "1")
                        .param("sort", "idItem")
                        .param("minqty", "50"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].idItem", is(response.getIdItem().intValue())))
                .andExpect(jsonPath("$.totalPages", is(pageResponse.getTotalPages())));

        when(loggedinUserInfo.getUser()).thenReturn(nonadmin);
        mvc
                .perform(get("/api/items")
                        .param("page", "0")
                        .param("limit", "1")
                        .param("sort", "idItem"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].idItem", is(response.getIdItem().intValue())))
                .andExpect(jsonPath("$.totalPages", is(pageResponse.getTotalPages())));

        mvc
                .perform(get("/api/items")
                        .param("page", "0")
                        .param("limit", "1")
                        .param("sort", "idItem")
                        .param("keyword", "item"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].idItem", is(response.getIdItem().intValue())))
                .andExpect(jsonPath("$.totalPages", is(pageResponse.getTotalPages())));

    }

    @Test
    public void readItemByIdItem() throws Exception {
        when(itemService.readItemByIdItem(1L)).thenReturn(item);
        when(itemMapper.entityToResponse(item)).thenReturn(response);
        mvc
                .perform(get("/api/items/" + item.getIdItem()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idItem", is(response.getIdItem().intValue())))
                .andExpect(jsonPath("$.itemName", is(response.getItemName())));
    }

    @Test
    public void readItemByItemName() throws Exception {
        when(loggedinUserInfo.getUser()).thenReturn(nonadmin);
        mvc
                .perform(get("/api/item")
                        .param("name", "item1"))
                .andDo(print())
                .andExpect(status().isUnauthorized());

        when(loggedinUserInfo.getUser()).thenReturn(admin);
        when(itemService.readItemByItemName("item1")).thenReturn(item);
        when(itemMapper.entityToResponse(item)).thenReturn(response);
        mvc
                .perform(get("/api/item")
                        .param("name", "item1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idItem", is(response.getIdItem().intValue())))
                .andExpect(jsonPath("$.itemName", is(response.getItemName())));
    }

    @Test
    public void updateItem() throws Exception {
        when(loggedinUserInfo.getUser()).thenReturn(nonadmin);
        mvc
                .perform(put("/api/items/" + item.getIdItem())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());

        when(loggedinUserInfo.getUser()).thenReturn(admin);
        when(itemMapper.requestToEntity(request)).thenReturn(item);
        when(itemService.updateItem(item)).thenReturn(item);
        when(itemMapper.entityToResponse(item)).thenReturn(response);
        mvc
                .perform(put("/api/items/" + item.getIdItem())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idItem", is(response.getIdItem().intValue())))
                .andExpect(jsonPath("$.itemName", is(response.getItemName())));
    }

    @Test
    public void deleteItem() throws Exception {
        when(loggedinUserInfo.getUser()).thenReturn(nonadmin);
        mvc
                .perform(delete("/api/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(deleteRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());

        when(loggedinUserInfo.getUser()).thenReturn(admin);
        when(itemService.deleteItem(deleteRequest.getIdItem())).thenReturn(ResponseEntity.ok().build());
        mvc
                .perform(delete("/api/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(deleteRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }
}