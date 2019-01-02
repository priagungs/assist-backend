package com.future.assist.service.service_impl;

import com.future.assist.exception.ConflictException;
import com.future.assist.exception.InvalidValueException;
import com.future.assist.exception.NotFoundException;
import com.future.assist.model.entity_model.Item;
import com.future.assist.repository.ItemRepository;
import com.future.assist.service.service_interface.ItemService;
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
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ItemServiceImplTest {

    private ItemService itemService;

    @MockBean
    private ItemRepository itemRepository;

    private Item item1;
    private Item item2;

    @Before
    public void setUp() {
        itemService = new ItemServiceImpl();

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

//    @Test(expected = ConflictException.class)
//    public void updateItemConflictException() {
//        Item newItem = item1;
//        newItem.setItemName("false name");
//        Mockito.when(itemRepository.findByIdItemAndIsActive(item1.getIdItem(), true))
//            .thenReturn(Optional.of(newItem));
//        Mockito.when(itemRepository.findByItemNameIgnoreCaseAndIsActive(item1.getItemName(), true))
//            .thenReturn(Optional.of(item1));
//        itemService.updateItem(item1);
//    }

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

//    @Test
//    public void deleteItemSuccess() {
//        Mockito.when(itemRepository.findByIdItemAndIsActive(item1.getIdItem(), true))
//            .thenReturn(Optional.of(item1));
//
//        Assert.assertEquals(ResponseEntity.ok().build(), itemService.deleteItem(item1.getIdItem()));
//    }
}
