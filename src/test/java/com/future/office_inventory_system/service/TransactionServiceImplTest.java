package com.future.office_inventory_system.service;

import com.future.office_inventory_system.exception.InvalidValueException;
import com.future.office_inventory_system.exception.NotFoundException;
import com.future.office_inventory_system.model.ItemTransaction;
import com.future.office_inventory_system.model.Transaction;
import com.future.office_inventory_system.model.User;
import com.future.office_inventory_system.repository.TransactionRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
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
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TransactionServiceImplTest {

    @Autowired
    TransactionService transactionService;

    @MockBean
    ItemTransactionService itemTransactionService;

    @MockBean
    UserService userService;

    @MockBean
    TransactionRepository repository;

    User user;
    ItemTransaction itemTransaction;
    Transaction transaction;


    @Before
    public void setUp() {
        user = new User();
        user.setIdUser(1L);

        itemTransaction = new ItemTransaction();
        itemTransaction.setIdItemTransaction(2L);
        List<ItemTransaction> itemTransactions = new ArrayList<>();
        itemTransactions.add(itemTransaction);

        transaction = new Transaction();
        transaction.setAdmin(user);
        transaction.setItemTransactions(itemTransactions);
    }

    @Test(expected = InvalidValueException.class)
    public void createTransactionInvalidValueTest() {
        Mockito.when(userService.readUserByIdUser(transaction.getAdmin().getIdUser()))
                .thenReturn(user);
        user.setIsAdmin(false);
        transactionService.createTransaction(transaction);
    }

    @Test
    public void createTransactionSuccessTest() {
        Mockito.when(userService.readUserByIdUser(transaction.getAdmin().getIdUser()))
                .thenReturn(user);
        Mockito.when(itemTransactionService.createItemTransaction(itemTransaction))
                .thenReturn(itemTransaction);
        Mockito.when(repository.save(transaction)).thenReturn(transaction);

        user.setIsAdmin(true);
        Assert.assertEquals(transaction, transactionService.createTransaction(transaction));
    }

    @Test
    public void readAllTransactionsTest() {
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);
        Page<Transaction> transactionPage =
                new PageImpl<Transaction>(transactions, PageRequest.of(0, Integer.MAX_VALUE), transactions.size());
        Mockito.when(repository.findAll(PageRequest.of(0, Integer.MAX_VALUE)))
                .thenReturn(transactionPage);

        Assert.assertEquals(transactionPage,
                transactionService.readAllTransactions(PageRequest.of(0, Integer.MAX_VALUE)));
    }

    @Test(expected = NotFoundException.class)
    public void readTransactionByIdTransactionNotFoundTest() {
        Mockito.when(repository.findById(transaction.getIdTransaction()))
                .thenReturn(Optional.empty());
        transactionService.readTransactionByIdTransaction(transaction.getIdTransaction());
    }

    @Test
    public void readTransactionByIdTransactionSuccessTest() {
        Mockito.when(repository.findById(transaction.getIdTransaction()))
                .thenReturn(Optional.of(transaction));

        Assert.assertEquals(transaction,
                transactionService.readTransactionByIdTransaction(transaction.getIdTransaction()));
    }

    @Test(expected = NotFoundException.class)
    public void deleteTransactionNotFoundTest() {
        Mockito.when(repository.findById(transaction.getIdTransaction()))
                .thenReturn(Optional.empty());
        transactionService.deleteTransaction(transaction.getIdTransaction());
    }

    @Test(expected = InvalidValueException.class)
    public void deleteTransactionInvalidValueTest() {
        Mockito.when(repository.findById(transaction.getIdTransaction()))
                .thenReturn(Optional.of(transaction));
        transaction.setTransactionDate(new Date(
                new Date().getTime() - TransactionService.MAX_ALLOWABLE_MILISECONDS_TO_UPDATE - 1));
        transactionService.deleteTransaction(transaction.getIdTransaction());
    }

    @Test
    public void deleteTransactionSuccessTest() {
        Mockito.when(repository.findById(transaction.getIdTransaction()))
                .thenReturn(Optional.of(transaction));
        Mockito.when(itemTransactionService.deleteItemTransaction(itemTransaction.getIdItemTransaction()))
                .thenReturn(ResponseEntity.ok().build());
        Mockito.doNothing().when(repository).delete(transaction);
        transaction.setTransactionDate(new Date());
        Assert.assertEquals(ResponseEntity.ok().build(),
                transactionService.deleteTransaction(transaction.getIdTransaction()));
    }
}