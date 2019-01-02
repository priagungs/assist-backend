package com.future.assist.service.service_impl;

import com.future.assist.exception.ForbiddenException;
import com.future.assist.exception.InvalidValueException;
import com.future.assist.exception.NotFoundException;
import com.future.assist.model.entity_model.ItemTransaction;
import com.future.assist.model.entity_model.Transaction;
import com.future.assist.model.entity_model.User;
import com.future.assist.repository.TransactionRepository;
import com.future.assist.service.service_interface.ItemTransactionService;
import com.future.assist.service.service_interface.TransactionService;
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
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TransactionServiceImplTest {

    @Autowired
    private TransactionService transactionService;

    @MockBean
    private ItemTransactionService itemTransactionService;

    @MockBean
    private UserService userService;

    @MockBean
    private TransactionRepository repository;

    private User user;
    private ItemTransaction itemTransaction;
    private Transaction transaction;


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
        Transaction newTrx = new Transaction();
        newTrx.setSupplier(transaction.getSupplier());
        newTrx.setTransactionDate(new Date());
        newTrx.setIdTransaction(transaction.getIdTransaction());

        Mockito.when(repository.save(any())).thenReturn(newTrx);
        user.setIsAdmin(true);

        Transaction result = transactionService.createTransaction(transaction);
        Assert.assertEquals(transaction.getIdTransaction(), result.getIdTransaction());
        Assert.assertEquals(transaction.getItemTransactions(), transaction.getItemTransactions());
        Assert.assertEquals(transaction.getSupplier(), transaction.getSupplier());
    }

    @Test
    public void readAllTransactionsTest() {
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);
        Page<Transaction> transactionPage =
                new PageImpl<>(transactions, PageRequest.of(0, Integer.MAX_VALUE), transactions.size());
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

    @Test(expected = ForbiddenException.class)
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