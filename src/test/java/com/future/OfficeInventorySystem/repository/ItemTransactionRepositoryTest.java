package com.future.OfficeInventorySystem.repository;

import com.future.OfficeInventorySystem.model.Item;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

@DataJpaTest
@RunWith(SpringRunner.class)
public class ItemTransactionRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ItemTransactionRepository itemTransactionRepository;

    private Item item;
    private ItemTransaction itemTransaction1;
    private ItemTransaction itemTransaction2;

    private Transaction transaction;


    @Before
    public void setUp() throws Exception {

        transaction = new Transaction();

        item = new Item();
        entityManager.persist(item);

        itemTransaction1 = new ItemTransaction();
        itemTransaction1.setTransaction(transaction);
        itemTransaction1.setItem(item);
        itemTransaction1.setBoughtQty(10);
        itemTransaction1.setPrice(new Long(1000));

        itemTransaction2 = new ItemTransaction();
        itemTransaction2.setTransaction(transaction);
        itemTransaction2.setItem(item);
        itemTransaction2.setBoughtQty(10);
        itemTransaction2.setPrice(new Long(1000));

        List list = new ArrayList();
        list.add(itemTransaction1);
        list.add(itemTransaction2);
        transaction.setItemTransaction(list);

        entityManager.persist(transaction);
        entityManager.persist(itemTransaction1);
        entityManager.persist(itemTransaction2);
    }

    @Test
    public void findByIdItemTransaction() {

    }

    @Test
    public void findAllByTransaction() {
        List list = new ArrayList();
        list.add(itemTransaction1);
        list.add(itemTransaction2);
        assertEquals(list, itemTransactionRepository.findAllByTransaction(transaction));

    }

    @Test
    public void findAllByItem() {
    }
}