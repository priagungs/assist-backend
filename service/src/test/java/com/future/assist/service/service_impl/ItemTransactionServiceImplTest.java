package com.future.assist.service.service_impl;

import com.future.assist.exception.InvalidValueException;
import com.future.assist.exception.NotFoundException;
import com.future.assist.model.entity_model.Item;
import com.future.assist.model.entity_model.ItemTransaction;
import com.future.assist.model.entity_model.Transaction;
import com.future.assist.repository.ItemRepository;
import com.future.assist.repository.ItemTransactionRepository;
import com.future.assist.service.service_interface.ItemService;
import com.future.assist.service.service_interface.ItemTransactionService;
import com.future.assist.service.service_interface.TransactionService;
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
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ItemTransactionServiceImplTest {

    @Autowired
    ItemTransactionService itemTransactionService;

    @MockBean
    ItemTransactionRepository itemTransactionRepository;

    @MockBean
    ItemService itemService;

    @MockBean
    TransactionService transactionService;

    @MockBean
    ItemRepository itemRepository;

    Transaction transaction;
    Item item1;
    Item item2;
    Item item3;
    ItemTransaction itemTransaction1;
    ItemTransaction itemTransaction2;
    ItemTransaction itemTransaction3;

    @Before
    public void setUp() {
        transaction = new Transaction();
        transaction.setIdTransaction(1L);

        item1 = new Item();
        item1.setIdItem(1L);
        item1.setAvailableQty(1);
        item1.setTotalQty(10);
        item2 = new Item();
        item2.setIdItem(2L);
        item3 = new Item();
        item3.setIdItem(1L);
        item3.setAvailableQty(100);
        item3.setTotalQty(1000);
        itemTransaction1 = new ItemTransaction();
        itemTransaction1.setIdItemTransaction(1L);
        itemTransaction1.setTransaction(transaction);
        itemTransaction1.setItem(item1);
        itemTransaction1.getItem().setTotalQty(1);
        itemTransaction1.setBoughtQty(10);
        itemTransaction1.setPrice(5000L);

        itemTransaction2 = new ItemTransaction();
        itemTransaction2.setIdItemTransaction(2L);
        itemTransaction2.setTransaction(transaction);
        itemTransaction2.setItem(item2);
        itemTransaction2.setBoughtQty(-10);
        itemTransaction2.setPrice(-5000L);

        itemTransaction3 = new ItemTransaction();
        itemTransaction3.setIdItemTransaction(3L);
        itemTransaction3.setTransaction(transaction);
        itemTransaction3.setItem(item3);
        itemTransaction3.getItem().setTotalQty(1000);
        itemTransaction3.setBoughtQty(1);
        itemTransaction3.setPrice(5000L);

    }

    @Test(expected = InvalidValueException.class)
    public void createItemTransactionInvalidValueTest() {
        Mockito.when(itemTransactionRepository
                .findById(itemTransaction2.getIdItemTransaction()))
                .thenReturn(Optional.empty());
        itemTransactionService.createItemTransaction(itemTransaction2);
    }

    @Test
    public void createItemTransactionSuccessTest() {
        Mockito.when(transactionService
                .readTransactionByIdTransaction(itemTransaction1.getTransaction().getIdTransaction()))
                .thenReturn(transaction);
        Mockito.when(itemService.readItemByIdItem(itemTransaction1.getItem().getIdItem()))
                .thenReturn(item1);
        Mockito.when(itemTransactionRepository.save(itemTransaction1))
                .thenReturn(itemTransaction1);
        Assert.assertEquals(itemTransaction1, itemTransactionService.createItemTransaction(itemTransaction1));
    }

    @Test(expected = NotFoundException.class)
    public void updateItemTransactionNotFoundTest() {
        Mockito.when(itemTransactionRepository
                .findById(itemTransaction1.getIdItemTransaction()))
                .thenReturn(Optional.empty());
        itemTransactionService.updateItemTransaction(itemTransaction1);
    }

    @Test(expected = InvalidValueException.class)
    public void updateItemTransactionInvalidValueTest() {
        Mockito.when(itemTransactionRepository
                .findById(itemTransaction2.getIdItemTransaction()))
                .thenReturn(Optional.of(itemTransaction2));
        itemTransactionService.updateItemTransaction(itemTransaction2);
    }

    @Test
    public void updateItemTransactionSuccessTest() {
        Mockito.when(itemTransactionRepository
                .findById(itemTransaction1.getIdItemTransaction()))
                .thenReturn(Optional.of(itemTransaction1));
        Mockito.when(itemTransactionRepository.save(itemTransaction1))
                .thenReturn(itemTransaction1);
        Assert.assertEquals(itemTransaction1, itemTransactionService.updateItemTransaction(itemTransaction1));
    }

    @Test
    public void readAllItemTransactionsTest() {
        PageImpl contents = new PageImpl(new ArrayList());

        Mockito.when(itemTransactionRepository.findAll(PageRequest.of(0, Integer.MAX_VALUE)))
                .thenReturn(contents);
        Assert.assertEquals(contents, itemTransactionService.readAllItemTransactions(PageRequest.of(0, Integer.MAX_VALUE)));
    }

    @Test(expected = NotFoundException.class)
    public void readItemTransactionByIdItemTransactionNotFoundTest() {
        Mockito.when(itemTransactionRepository.findById(itemTransaction1.getIdItemTransaction()))
                .thenReturn(Optional.empty());
        itemTransactionService.readItemTransactionByIdItemTransaction(itemTransaction1.getIdItemTransaction());
    }

    @Test
    public void readItemTransactionByIdItemTransactionSuccessTest() {
        Mockito.when(itemTransactionRepository.findById(itemTransaction1.getIdItemTransaction()))
                .thenReturn(Optional.of(itemTransaction1));
        Assert.assertEquals(itemTransaction1, itemTransactionService.readItemTransactionByIdItemTransaction(itemTransaction1.getIdItemTransaction()));
    }


    @Test(expected = NotFoundException.class)
    public void readAllItemTransactionsByTransactionNotFoundTest() {
        Mockito.when(itemTransactionRepository.findAllByTransaction(transaction, PageRequest.of(0, Integer.MAX_VALUE)))
                .thenReturn(Page.empty());
        itemTransactionService.readAllItemTransactionsByTransaction(transaction, PageRequest.of(0, Integer.MAX_VALUE));
    }

    @Test
    public void readAllItemTransactionsByTransactionSuccessTest() {
        List<ItemTransaction> itemTransactions = new ArrayList<>();
        itemTransactions.add(itemTransaction1);
        itemTransactions.add(itemTransaction2);
        itemTransactions.add(itemTransaction3);

        Page<ItemTransaction> itemTransactionPage =
                new PageImpl<ItemTransaction>(itemTransactions, PageRequest.of(0, Integer.MAX_VALUE), itemTransactions.size());

        Mockito.when(itemTransactionRepository.findAllByTransaction(transaction, PageRequest.of(0, Integer.MAX_VALUE)))
                .thenReturn(itemTransactionPage);
        Assert.assertEquals(itemTransactionPage,
                itemTransactionService.readAllItemTransactionsByTransaction(transaction, PageRequest.of(0, Integer.MAX_VALUE)));
    }

    @Test(expected = NotFoundException.class)
    public void deleteItemTransactionNotFoundTest() {
        Mockito.when(itemTransactionRepository.findById(itemTransaction1.getIdItemTransaction()))
                .thenReturn(Optional.empty());
        itemTransactionService.deleteItemTransaction(itemTransaction1.getIdItemTransaction());
    }

    @Test(expected = InvalidValueException.class)
    public void deleteItemTransactionInvalidValueTest() {
        Mockito.when(itemTransactionRepository.findById(itemTransaction1.getIdItemTransaction()))
                .thenReturn(Optional.of(itemTransaction1));
        itemTransactionService.deleteItemTransaction(itemTransaction1.getIdItemTransaction());

    }

    @Test
    public void deleteItemTransactionSuccess() {
        Mockito.when(itemTransactionRepository.findById(itemTransaction3.getIdItemTransaction()))
                .thenReturn(Optional.of(itemTransaction3));
        Assert.assertEquals(ResponseEntity.ok().build(), itemTransactionService.deleteItemTransaction(itemTransaction3.getIdItemTransaction()));
    }
}