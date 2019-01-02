package com.future.assist.service.service_impl;

import com.future.assist.exception.InvalidValueException;
import com.future.assist.exception.NotFoundException;
import com.future.assist.model.entity_model.Item;
import com.future.assist.model.entity_model.User;
import com.future.assist.model.entity_model.UserHasItem;
import com.future.assist.repository.UserHasItemRepository;
import com.future.assist.service.service_interface.ItemService;
import com.future.assist.service.service_interface.UserHasItemService;
import com.future.assist.service.service_interface.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserHasItemServiceImplTest {

    @Autowired
    private UserHasItemService userHasItemService;

    @MockBean
    private UserHasItemRepository userHasItemRepository;

    @MockBean
    private ItemService itemService;

    @MockBean
    private UserService userService;

    private User user;
    private Item item;
    private UserHasItem userHasItem;

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
        userHasItem.setHasQty(2);
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
        assertEquals(userHasItem, userHasItemService.createUserHasItem(userHasItem));
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

        assertEquals(userHasItem, userHasItemService.createUserHasItem(userHasItem));
    }

    @Test
    public void createUserHasItemFromRequestSuccessTest() {
        List<UserHasItem> listUHI = new ArrayList<>();

        listUHI.add(userHasItem);

        Mockito.when(userHasItemRepository.findAllByUserAndItem(userHasItem.getUser(), userHasItem.getItem()))
                .thenReturn(listUHI);

        Mockito.when(userHasItemRepository.save(userHasItem))
                .thenReturn(userHasItem);

        Mockito.when(userHasItemRepository.findById(userHasItem.getIdUserHasItem()))
                .thenReturn(Optional.of(userHasItem));

        UserHasItem result = userHasItemService.createUserHasItemFromRequest(userHasItem);

        System.out.println(userHasItem.getUser().getIdUser());
        System.out.println(result.getUser().getIdUser());
        assertEquals(userHasItem.getUser().getIdUser(), result.getUser().getIdUser());
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
        assertEquals(userHasItem,
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
        Mockito.when(itemService.updateItem(userHasItem.getItem())).thenReturn(userHasItem.getItem());
        Mockito.when(userService.readUserByIdUser(userHasItem.getUser().getIdUser())).thenReturn(user);

        userHasItem.getUser().setRequests(new ArrayList<>());
        userHasItem.setHasQty(1);
        item.setAvailableQty(1);
        assertEquals(ResponseEntity.ok().build(),
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

        assertEquals(userHasItem, userHasItemService.updateUserHasItem(userHasItem));
    }

    @Test
    public void readAllUserHasItems() {
        assertTrue(true);
    }

    @Test
    public void readAllUserHasItemsTest() {
        List<UserHasItem> userHasItems = new ArrayList<>();
        userHasItems.add(userHasItem);
        PageImpl<UserHasItem> userHasItemPage = new PageImpl(userHasItems, PageRequest.of(0, Integer.MAX_VALUE),
                userHasItems.size());
        Mockito.when(userHasItemRepository.findAll(PageRequest.of(0, Integer.MAX_VALUE)))
                .thenReturn(userHasItemPage);

        assertEquals(userHasItemPage,
                userHasItemService.readAllUserHasItems(PageRequest.of(0, Integer.MAX_VALUE)));

    }

    @Test
    public void readAllUserHasItemsByIdUser() {
        assertTrue(true);
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

        assertEquals(userHasItemPage, userHasItemService.readAllUserHasItemsByIdUser(user.getIdUser(),
                PageRequest.of(0, Integer.MAX_VALUE)));
    }

    @Test
    public void readAllUserHasItemsByIdItemTest() {
        Mockito.when(itemService.readItemByIdItem(item.getIdItem()))
                .thenReturn(item);

        List<UserHasItem> userHasItems = new ArrayList<>();
        userHasItems.add(userHasItem);
        PageImpl<UserHasItem> userHasItemPage = new PageImpl(userHasItems, PageRequest.of(0, Integer.MAX_VALUE),
                userHasItems.size());
        Mockito.when(userHasItemRepository.findAllByItem(item, PageRequest.of(0, Integer.MAX_VALUE)))
                .thenReturn(userHasItemPage);

        assertEquals(userHasItemPage, userHasItemService.readAllUserHasItemsByIdItem(item.getIdItem(),
                PageRequest.of(0, Integer.MAX_VALUE)));
    }
}