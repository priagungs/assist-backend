package com.future.assist.printer;

import com.future.assist.model.entity_model.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PrinterServiceTest {
    
    private Transaction transaction;
    private Item item;
    private User user;
    private ItemTransaction itemTransaction;
    private UserHasItem userHasItem;
    
    @Before
    public void setUp() {
        item = new Item();
        item.setIdItem(5L);
        item.setItemName("indomie");
        item.setPictureURL("www.link.foto");
        item.setPrice(new Long(98765));
        item.setTotalQty(100);
        item.setAvailableQty(90);
        item.setDescription("ini indomie enaq");
        item.setRequests(new ArrayList<>());
        item.setOwners(new ArrayList<>());
        item.setIsActive(true);
        
        user = new User();
        user.setUsername("bambang");
        user.setName("Bambang Nugroho");
        user.setIdUser(4L);
        user.setIsAdmin(true);
        user.setSuperior(null);
        user.setIsActive(true);
        user.setPictureURL("picture.img");
        user.setPassword("asdfasdf");
        user.setDivision("Engineering");
        user.setRole("Software Engineer");
        
        userHasItem = new UserHasItem();
        userHasItem.setItem(item);
        userHasItem.setUser(user);
        userHasItem.setHasQty(10);
        userHasItem.setIdUserHasItem(6L);
        List<UserHasItem> userHasItems = new ArrayList<>();
        userHasItems.add(userHasItem);
        item.setOwners(userHasItems);
    
        itemTransaction = new ItemTransaction();
        itemTransaction.setIdItemTransaction(2L);
        itemTransaction.setItem(item);
        itemTransaction.setBoughtQty(5);
        itemTransaction.setPrice(item.getPrice());
        List<ItemTransaction> itemTransactions = new ArrayList<>();
        itemTransactions.add(itemTransaction);
    
        transaction = new Transaction();
        transaction.setAdmin(user);
        transaction.setItemTransactions(itemTransactions);
        transaction.setIdTransaction(1L);
        transaction.setSupplier("Indofood");
        transaction.setTransactionDate(new Date());
    }
    
    @Test
    public void printInvoice() {
        PrinterService.printInvoice(transaction);
        Assert.assertEquals(transaction, transaction);
    }
    
    @Test
    public void printItem() {
        PrinterService.printItem(item);
        Assert.assertEquals(item, item);
    }
}