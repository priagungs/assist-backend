package com.future.assist.service.service_impl;

import com.future.assist.exception.ConflictException;
import com.future.assist.exception.InvalidValueException;
import com.future.assist.exception.NotFoundException;
import com.future.assist.model.RequestStatus;
import com.future.assist.model.entity_model.Item;
import com.future.assist.model.entity_model.Request;
import com.future.assist.model.entity_model.User;
import com.future.assist.repository.ItemRepository;
import com.future.assist.service.service_interface.ItemService;
import com.future.assist.service.service_interface.RequestService;
import com.future.assist.service.service_interface.UserHasItemService;
import com.future.assist.service.service_interface.UserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ItemServiceImplTest {

    @Autowired
    private ItemService itemService;

    @MockBean
    private ItemRepository itemRepository;
    
    @MockBean
    private RequestService requestService;
    
    @MockBean
    private LoggedinUserInfo loggedinUserInfo;

    @MockBean
    private UserHasItemService userHasItemService;
    
    private Item item1;
    private Item item2;
    private Request request1;
    private Request request2;
    private User user;

    @Before
    public void setUp() {
        item1 = new Item();
        item1.setItemName("indomie");
        item1.setPictureURL("www.link.foto");
        item1.setPrice(new Long(98765));
        item1.setTotalQty(100);
        item1.setAvailableQty(90);
        item1.setDescription("ini indomie enaq");
        item1.setRequests(new ArrayList<>());
        item1.setOwners(new ArrayList<>());
        item1.setIsActive(true);

        item2 = new Item();
        item2.setItemName("BB Cream");
        item2.setPictureURL("www.link.foto");
        item2.setPrice(new Long(-1000));
        item2.setTotalQty(-2);
        item2.setAvailableQty(5);
        item2.setDescription("BB Cream biar syantiq");
        item2.setRequests(new ArrayList<>());
        item2.setOwners(new ArrayList<>());
        item2.setIsActive(true);
        
        request1 = new Request();
        request1.setRequestStatus(RequestStatus.APPROVED);
        
        request2 = new Request();
        request2.setRequestStatus(RequestStatus.SENT);
        
        user = new User();
        user.setIdUser(1L);
    }

    @Test(expected = ConflictException.class)
    public void createItemAlreadyExistTest() {
        Mockito.when(itemRepository.findByItemNameIgnoreCaseAndIsActive(item1.getItemName(), true))
                .thenReturn(Optional.of(item1));
        itemService.createItem(item1);
    }

    @Test(expected = InvalidValueException.class)
    public void createItemInvalidValueTest() {
        Mockito.when(itemRepository.findByItemNameIgnoreCaseAndIsActive(item2.getItemName(), true))
                .thenReturn(Optional.empty());
        itemService.createItem(item2);
    }

    @Test
    public void createItemSuccessTest() {
        Mockito.when(itemRepository.save(item1)).thenReturn(item1);
        Mockito.when(itemRepository.findByItemNameIgnoreCaseAndIsActive(item1.getItemName(), true))
                .thenReturn(Optional.empty());
        Assert.assertEquals(item1, itemService.createItem(item1));
    }

    @Test(expected = NotFoundException.class)
    public void updateItemNotFoundTest() {
        Mockito.when(itemRepository.findByIdItemAndIsActive(item1.getIdItem(), true))
                .thenReturn(Optional.empty());
        itemService.updateItem(item1);
    }

    @Test(expected = InvalidValueException.class)
    public void updateItemInvalidValueTest() {
        Mockito.when(itemRepository.findByIdItemAndIsActive(item2.getIdItem(), true))
                .thenReturn(Optional.of(item2));
        itemService.updateItem(item2);
    }

    @Test(expected = ConflictException.class)
    public void updateItemConflictException() {
        Item newItem = new Item();
        newItem.setIdItem(item1.getIdItem());
        newItem.setItemName("false name");
        newItem.setAvailableQty(item1.getAvailableQty());
        newItem.setTotalQty(item1.getTotalQty());
        Mockito.when(itemRepository.findByIdItemAndIsActive(item1.getIdItem(), true))
            .thenReturn(Optional.of(newItem));
        Mockito.when(itemRepository.findByItemNameIgnoreCaseAndIsActive(item1.getItemName(), true))
            .thenReturn(Optional.of(item1));
        itemService.updateItem(item1);
    }

    @Test(expected = InvalidValueException.class)
    public void updateItemInvalidValueTest2() {
        item1.setAvailableQty(-2);
        Mockito.when(itemRepository.findByIdItemAndIsActive(item1.getIdItem(), true))
                .thenReturn(Optional.of(item1));
        itemService.updateItem(item1);
    }

    @Test
    public void updateItemSuccessTest() {
        Mockito.when(itemRepository.save(item1)).thenReturn(item1);
        Mockito.when(itemRepository.findByIdItemAndIsActive(item1.getIdItem(), true))
                .thenReturn(Optional.of(item1));
        Assert.assertEquals(item1, itemService.updateItem(item1));
    }

    @Test
    public void readAllItemsTest() {
        PageImpl contents = new PageImpl(new ArrayList());

        Mockito.when(itemRepository.findAllByIsActive(true, PageRequest.of(0, Integer.MAX_VALUE)))
                .thenReturn(contents);
        Assert.assertEquals(contents, itemService.readAllItems(PageRequest.of(0, Integer.MAX_VALUE)));
    }

    @Test(expected = NotFoundException.class)
    public void readItemByIdItemNotFoundTest() {
        Mockito.when(itemRepository.findByIdItemAndIsActive(item1.getIdItem(), true))
                .thenReturn(Optional.empty());
        itemService.readItemByIdItem(item1.getIdItem());
    }

    @Test
    public void readItemByIdItemSuccessTest() {
        Mockito.when(itemRepository.findByIdItemAndIsActive(item1.getIdItem(), true))
                .thenReturn(Optional.of(item1));
        Assert.assertEquals(item1, itemService.readItemByIdItem(item1.getIdItem()));
    }

    @Test(expected = InvalidValueException.class)
    public void readItemsByAvailableGreaterThanInvalidValueTest() {
        itemService.readItemsByAvailableGreaterThan(-5, PageRequest.of(0, Integer.MAX_VALUE));
    }

    @Test
    public void readItemsByAvailableGreaterThanSuccessTest() {
        List<Item> items = new ArrayList<>();
        items.add(item1);
        items.add(item2);


        Page<Item> itemPage =
                new PageImpl<Item>(items, PageRequest.of(0, Integer.MAX_VALUE), items.size());
        Mockito.when(itemRepository.findAllByAvailableQtyGreaterThanAndIsActive(0, true, PageRequest.of(0, Integer.MAX_VALUE)))
                .thenReturn(itemPage);

        Assert.assertEquals(itemPage, itemService.readItemsByAvailableGreaterThan(0, PageRequest.of(0, Integer.MAX_VALUE)));
    }

    @Test
    public void readAllItemsContainingSuccessTest() {
        List<Item> items = new ArrayList<>();
        items.add(item1);
        Page<Item> contents = new PageImpl(items);

        Mockito.when(itemRepository.findByItemNameIgnoreCaseContainingAndIsActive(
                "a", true, PageRequest.of(0, 2)))
                .thenReturn(contents);
        Assert.assertEquals(contents, itemService.readAllItemsContaining("a", PageRequest.of(0, 2)));
    }

    @Test
    public void readAllItemsByKeywordAndAvailableGreaterThanSuccessTest() {
        List<Item> items = new ArrayList<>();
        items.add(item1);
        PageImpl contents = new PageImpl(items);

        Mockito.when(itemRepository
                .findByItemNameIgnoreCaseContainingAndAvailableQtyGreaterThanAndIsActive(
                        item1.getItemName(), 0, true, PageRequest.of(0, 2)))
                .thenReturn(contents);
        Assert.assertEquals(contents, itemService
                .readAllItemsByKeywordAndAvailableGreaterThan(
                        item1.getItemName(), 0, PageRequest.of(0, 2)));
    }

    @Test(expected = NotFoundException.class)
    public void readItemByItemNameNotFoundTest() {
        Mockito.when(itemRepository.findByItemNameIgnoreCaseAndIsActive(item1.getItemName(), true))
                .thenReturn(Optional.empty());
        itemService.readItemByItemName(item1.getItemName());
    }

    @Test
    public void readItemByItemNameSuccessTest() {
        Mockito.when(itemRepository.findByItemNameIgnoreCaseAndIsActive(item1.getItemName(), true))
                .thenReturn(Optional.of(item1));
        Assert.assertEquals(item1, itemService.readItemByItemName(item1.getItemName()));
    }

    @Test(expected = NotFoundException.class)
    public void deleteItemNotFoundTest() {
        Mockito.when(itemRepository.findByIdItemAndIsActive(item1.getIdItem(), true))
                .thenReturn(Optional.empty());
        itemService.deleteItem(item1.getIdItem());
    }

    @Test
    public void deleteItemSuccess() {
        Mockito.when(itemRepository.findByIdItemAndIsActive(item1.getIdItem(), true))
            .thenReturn(Optional.of(item1));
        List<Request> requests = new ArrayList<>();
        requests.add(request1);
        requests.add(request2);
        Mockito.when(requestService.readAllRequestsByItem(item1)).thenReturn(requests);
        Mockito.when(requestService.updateRequestByRequestObject(request1)).thenReturn(request1);
        Mockito.when(requestService.updateRequestByRequestObject(request2)).thenReturn(request2);
        Mockito.when(loggedinUserInfo.getUser()).thenReturn(user);
        Mockito.when(userHasItemService.readAllUserHasItemsByIdItem(item1.getIdItem(), PageRequest.of(0,
            Integer.MAX_VALUE))).thenReturn(new PageImpl<>(new ArrayList<>()));
        Assert.assertEquals(ResponseEntity.ok().build(), itemService.deleteItem(item1.getIdItem()));
    }
}
