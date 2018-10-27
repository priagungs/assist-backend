package com.future.office_inventory_system.service;

import com.future.office_inventory_system.exception.InvalidValueException;
import com.future.office_inventory_system.exception.NotFoundException;
import com.future.office_inventory_system.model.Item;
import com.future.office_inventory_system.model.User;
import com.future.office_inventory_system.model.UserHasItem;
import com.future.office_inventory_system.repository.UserHasItemRepository;
import org.h2.store.Page;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.omg.CORBA.DynAnyPackage.Invalid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.awt.print.Pageable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserHasItemServiceImplTest {

    @Autowired
    UserHasItemService userHasItemService;

    @MockBean
    UserHasItemRepository userHasItemRepository;

    @MockBean
    ItemService itemService;

    @MockBean
    UserService userService;

    User user;
    Item item;
    UserHasItem userHasItem;

    @Before
    public void setUp() {
        user = new User();
        user.setIdUser(1L);
        item = new Item();
        item.setIdItem(2L);
        userHasItem = new UserHasItem();
        userHasItem.setItem(item);
        userHasItem.setUser(user);
        userHasItem.setIdUserHasItem(1L);
    }

    @Test(expected = InvalidValueException.class)
    public void createUserHasItemInvalidValueTest() {
        Mockito.when(userService.readUserByIdUser(userHasItem.getUser().getIdUser()))
                .thenReturn(user);
        Mockito.when(itemService.readItemByIdItem(userHasItem.getItem().getIdItem()))
                .thenReturn(item);
        Mockito.when(userHasItemRepository.findAllByUserAndItem(user, item))
                .thenReturn(new ArrayList<>());

        userHasItem.setHasQty(2);
        item.setAvailableQty(1);

        userHasItemService.createUserHasItem(userHasItem);
    }

    @Test
    public void createUserHasItemSuccessTest() {
        Mockito.when(userService.readUserByIdUser(userHasItem.getUser().getIdUser()))
                .thenReturn(user);
        Mockito.when(itemService.readItemByIdItem(userHasItem.getItem().getIdItem()))
                .thenReturn(item);
        Mockito.when(userHasItemRepository.findAllByUserAndItem(user, item))
                .thenReturn(new ArrayList<>());
        Mockito.when(userHasItemRepository.save(userHasItem)).thenReturn(userHasItem);

        userHasItem.setHasQty(1);
        item.setAvailableQty(1);
        Assert.assertEquals(userHasItem, userHasItemService.createUserHasItem(userHasItem));
    }

    @Test
    public void createUserHasItemUpdateTest() {
        Mockito.when(userService.readUserByIdUser(userHasItem.getUser().getIdUser()))
                .thenReturn(user);
        Mockito.when(itemService.readItemByIdItem(userHasItem.getItem().getIdItem()))
                .thenReturn(item);

        List<UserHasItem> userHasItems = new ArrayList<>();
        userHasItems.add(userHasItem);
        Mockito.when(userHasItemRepository.findAllByUserAndItem(user, item))
                .thenReturn(userHasItems);

        Mockito.when(userHasItemRepository.findById(userHasItem.getIdUserHasItem()))
                .thenReturn(Optional.of(userHasItem));
        Mockito.when(userHasItemRepository.save(userHasItem)).thenReturn(userHasItem);

        userHasItem.setHasQty(1);
        item.setAvailableQty(1);

        Assert.assertEquals(userHasItem, userHasItemService.createUserHasItem(userHasItem));
    }

    @Test(expected = NotFoundException.class)
    public void readUserHasItemByIdNotFoundTest() {
        Mockito.when(userHasItemRepository.findById(userHasItem.getIdUserHasItem()))
                .thenReturn(Optional.empty());
        userHasItemService.readUserHasItemById(userHasItem.getIdUserHasItem());
    }

    @Test
    public void readUserHasItemByIdFoundTest() {
        Mockito.when(userHasItemRepository.findById(userHasItem.getIdUserHasItem()))
                .thenReturn(Optional.of(userHasItem));
        Assert.assertEquals(userHasItem,
                userHasItemService.readUserHasItemById(userHasItem.getIdUserHasItem()));
    }

    @Test(expected = NotFoundException.class)
    public void deleteUserHasItemNotFoundTest() {
        Mockito.when(userHasItemRepository.findById(userHasItem.getIdUserHasItem()))
                .thenReturn(Optional.empty());
        userHasItemService.deleteUserHasItem(userHasItem.getIdUserHasItem());
    }

    @Test
    public void deleteUserHasItemSuccessTest() {
        Mockito.when(userHasItemRepository.findById(userHasItem.getIdUserHasItem()))
                .thenReturn(Optional.of(userHasItem));
        Mockito.doNothing().when(userHasItemRepository).delete(userHasItem);

        userHasItem.setHasQty(1);
        item.setAvailableQty(1);
        Assert.assertEquals(ResponseEntity.ok().build(),
                userHasItemService.deleteUserHasItem(userHasItem.getIdUserHasItem()));
    }

    @Test(expected = NotFoundException.class)
    public void updateUserHasItemNotFoundTest() {
        Mockito.when(userHasItemRepository.findById(userHasItem.getIdUserHasItem()))
                .thenReturn(Optional.empty());
        userHasItemService.updateUserHasItem(userHasItem);
    }

    @Test(expected = InvalidValueException.class)
    public void updateUserHasItemInvalidValueTest() {
        Mockito.when(userHasItemRepository.findById(userHasItem.getIdUserHasItem()))
                .thenReturn(Optional.of(userHasItem));
        Mockito.when(itemService.readItemByIdItem(item.getIdItem())).thenReturn(item);

        userHasItem.setHasQty(2);
        item.setAvailableQty(-1);

        userHasItemService.updateUserHasItem(userHasItem);
    }

    @Test
    public void updateUserHasItemSuccessTest() {
        Mockito.when(userHasItemRepository.findById(userHasItem.getIdUserHasItem()))
                .thenReturn(Optional.of(userHasItem));
        Mockito.when(userService.readUserByIdUser(userHasItem.getUser().getIdUser()))
                .thenReturn(user);
        Mockito.when(itemService.readItemByIdItem(userHasItem.getItem().getIdItem()))
                .thenReturn(item);
        Mockito.when(userHasItemRepository.save(userHasItem)).thenReturn(userHasItem);

        userHasItem.setHasQty(2);
        item.setAvailableQty(2);

        Assert.assertEquals(userHasItem, userHasItemService.updateUserHasItem(userHasItem));
    }

    @Test
    public void readAllUserHasItemsTest() {
        List<UserHasItem> userHasItems = new ArrayList<>();
        userHasItems.add(userHasItem);
        PageImpl<UserHasItem> userHasItemPage = new PageImpl(userHasItems, PageRequest.of(0, Integer.MAX_VALUE),
                userHasItems.size());
        Mockito.when(userHasItemRepository.findAll(PageRequest.of(0, Integer.MAX_VALUE)))
                .thenReturn(userHasItemPage);

        Assert.assertEquals(userHasItemPage,
                userHasItemService.readAllUserHasItems(PageRequest.of(0, Integer.MAX_VALUE)));

    }

    @Test
    public void readAllUserHasItemsByIdUserTest() {
        Mockito.when(userService.readUserByIdUser(user.getIdUser()))
                .thenReturn(user);

        List<UserHasItem> userHasItems = new ArrayList<>();
        userHasItems.add(userHasItem);
        PageImpl<UserHasItem> userHasItemPage = new PageImpl(userHasItems, PageRequest.of(0, Integer.MAX_VALUE),
                userHasItems.size());
        Mockito.when(userHasItemRepository.findAllByUser(user, PageRequest.of(0, Integer.MAX_VALUE)))
                .thenReturn(userHasItemPage);

        Assert.assertEquals(userHasItemPage, userHasItemService.readAllUserHasItemsByIdUser(user.getIdUser(),
                PageRequest.of(0,  Integer.MAX_VALUE)));
    }

    @Test
    public void readAllUserHasItemsByIdItem() {
        Mockito.when(itemService.readItemByIdItem(item.getIdItem()))
                .thenReturn(item);

        List<UserHasItem> userHasItems = new ArrayList<>();
        userHasItems.add(userHasItem);
        PageImpl<UserHasItem> userHasItemPage = new PageImpl(userHasItems, PageRequest.of(0, Integer.MAX_VALUE),
                userHasItems.size());
        Mockito.when(userHasItemRepository.findAllByItem(item, PageRequest.of(0, Integer.MAX_VALUE)))
                .thenReturn(userHasItemPage);

        Assert.assertEquals(userHasItemPage, userHasItemService.readAllUserHasItemsByIdItem(item.getIdItem(),
                PageRequest.of(0,  Integer.MAX_VALUE)));
    }

}