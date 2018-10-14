package com.future.OfficeInventorySystem.repository;

import com.future.OfficeInventorySystem.model.ItemTransaction;
import com.future.OfficeInventorySystem.model.Transaction;
import com.future.OfficeInventorySystem.model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

@DataJpaTest
@RunWith(SpringRunner.class)
public class TransactionTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TransactionRepository transactionRepository;

    private User admin;
    private Transaction transaction1;
    private Transaction transaction2;
    private Transaction transaction3;


    @Before
    public void setUp() throws Exception {
        admin = new User();
        admin.setUsername("administrator");
        admin.setPassword("12345678");
        admin.setName("Administrator");
        admin.setIsAdmin(true);
        entityManager.persist(admin);

        transaction1 = new Transaction();
        transaction1.setTransactionDate(new Date());
        transaction1.setSupplier("Indofood");
        transaction1.setAdmin(admin);

        transaction2 = new Transaction();
        transaction2.setTransactionDate(new Date());
        transaction2.setSupplier("Indofood");
        transaction2.setAdmin(admin);

        transaction3 = new Transaction();
        transaction3.setTransactionDate(new Date());
        transaction3.setSupplier("Indofood");
        transaction3.setAdmin(admin);

        entityManager.persist(transaction1);
        entityManager.persist(transaction2);
        entityManager.persist(transaction3);

    }

    @Test
    public void findByIdTransaction() {
        Long id = Long.valueOf(18216004);
        assertEquals(transaction1, transactionRepository.findByIdTransaction(id));

    }

    @Test
    public void findAllByAdmin() {
        List<Transaction> list = new ArrayList<>();
        list.add(transaction1);
        list.add(transaction2);
        list.add(transaction3);

        assertEquals(list, transactionRepository.findAllByAdmin(admin));

    }
}