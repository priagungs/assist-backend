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
public class ItemTransactionTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ItemTransactionRepository itemTransactionRepository;

    private Item item;
    private User user;
    private ItemTransaction itemTransaction1;
    private ItemTransaction itemTransaction2;

    private Transaction transaction;


    @Before
    public void setUp() throws Exception {

        user = new User();
        entityManager.persist(user);

        transaction = new Transaction();
        transaction.setAdmin(user);
        entityManager.persist(transaction);

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

        List<ItemTransaction> list = new ArrayList<>();
        list.add(itemTransaction1);
        list.add(itemTransaction2);
        transaction.setItemTransaction(list);


        entityManager.persist(itemTransaction1);
        entityManager.persist(itemTransaction2);
    }

    @Test
    public void findByIdItemTransaction() {
        assertEquals(itemTransaction1,
                itemTransactionRepository.findByIdItemTransaction(
                        itemTransaction1.getIdItemTransaction()
                ));
        assertEquals(itemTransaction2,
                itemTransactionRepository.findByIdItemTransaction(
                        itemTransaction2.getIdItemTransaction()
                ));

    }

    @Test
    public void findAllByTransaction() {
        List<ItemTransaction> listitem = new ArrayList<>();
        listitem.add(itemTransaction1);
        listitem.add(itemTransaction2);
        assertEquals(listitem, itemTransactionRepository.findAllByTransaction(transaction));

    }

    @Test
    public void findAllByItem() {
        List<ItemTransaction> listitem = new ArrayList<>();
        listitem.add(itemTransaction1);
        listitem.add(itemTransaction2);
        assertEquals(listitem, itemTransactionRepository.findAllByItem(item));
    }


}