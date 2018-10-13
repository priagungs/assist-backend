package com.future.OfficeInventorySystem.repository;

import com.future.OfficeInventorySystem.model.ItemTransaction;
import com.future.OfficeInventorySystem.model.Transaction;
import com.future.OfficeInventorySystem.model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.junit.Assert.*;

@DataJpaTest
@RunWith(SpringRunner.class)
public class TransactionTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TransactionRepository userRepository;

    @Autowired
    private User admin;

    @Autowired
    private ItemTransaction itemTransaction;

    private Transaction transaction1;
    private Transaction transaction2;
    private Transaction transaction3;

    @Before
    public void setUp() throws Exception {
        transaction1 = new Transaction();
        transaction1.setTransactionDate(new Date());
        transaction1.setSupplier("Indofood");
        transaction1.setAdmin(admin);

    }

    @Test
    public void findByIdTransaction() {
    }

    @Test
    public void findAllByAdmin() {
    }
}