package com.future.office_inventory_system.service;

import com.future.office_inventory_system.model.Item;
import com.future.office_inventory_system.repository.ItemRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Optional;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ItemServiceImplTest {
    
    @Autowired
    ItemService itemService;
    
    @MockBean
    ItemRepository itemRepository;
    
    @MockBean
    RequestService requestService;
    
    @MockBean
    UserHasItemService userHasItemService;
    
    @MockBean
    ItemTransactionService itemTransactionService;
    
    private Item item1;
    private Item item2;
    
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
        item2.setPrice(new Long(54321));
        item2.setTotalQty(100);
        item2.setAvailableQty(90);
        item2.setDescription("BB Cream biar syantiq");
        item2.setRequests(new ArrayList<>());
        item2.setOwners(new ArrayList<>());
        item2.setIsActive(true);
    }
    
    @Test
    public void createItemSuccessTest() {
    }
    
    @Test
    public void updateItem() {
    }
    
    @Test
    public void readAllItems() {
    }
    
    @Test
    public void readItemByIdItem() {
    }
    
    @Test
    public void readItemsByAvailableGreaterThan() {
    }
    
    @Test
    public void deleteItemNotFoundTest() {
        Mockito.when(itemRepository.findById(item1.getIdItem()))
            .thenReturn(Optional.empty());
        itemService.readItemByIdItem(item1.getIdItem());
    }
    
    @Test
    public void deleteItemSuccess() {
        Mockito.when(itemRepository.findById(item1.getIdItem()))
            .thenReturn(Optional.of(item1));
        Assert.assertEquals(ResponseEntity.ok().build(), itemService.deleteItem(item1.getIdItem()));
    }
}