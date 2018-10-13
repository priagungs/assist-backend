package com.future.OfficeInventorySystem.repository;

import com.future.OfficeInventorySystem.model.ItemTransaction;
import com.future.OfficeInventorySystem.model.Transaction;
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
public class ItemTransactionRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ItemTransactionRepository itemTransactionRepository;

    private ItemTransaction itemTransaction1;
    private ItemTransaction itemTransaction2;

    private Transaction transaction1;
    private Transaction transaction2;

    @Before
    public void setUp() throws Exception {
        Transaction transaction1 = new Transaction();
        transaction1.setTransactionDate(new Date);
        transaction1.setSupplier("Indofood");
        transaction1

        itemTransaction1 = new ItemTransaction();
        itemTransaction1.setTransaction(transaction1);

    }

    @Test
    public void findByIdItemTransaction() {
    }

    @Test
    public void findAllByTransaction() {
    }

    @Test
    public void findAllByItem() {
    }
}