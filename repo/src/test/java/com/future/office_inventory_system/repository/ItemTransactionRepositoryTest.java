package com.future.office_inventory_system.repository;

import com.future.office_inventory_system.model.Item;
import com.future.office_inventory_system.model.ItemTransaction;
import com.future.office_inventory_system.model.Transaction;
import com.future.office_inventory_system.model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

//@RunWith(SpringRunner.class)
@DataJpaTest
@SpringBootTest
//@EnableJpaRepositories("com.future.office_inventory_system.repository")
public class ItemTransactionRepositoryTest {

    @Autowired
    public TestEntityManager entityManager;

    @Autowired
    public ItemTransactionRepository transactionRepository;

    private Item item;
    private User user;
    private ItemTransaction itemTransaction1;
    private ItemTransaction itemTransaction2;

    private Transaction transaction;


    @Before
    public void setUp() throws Exception {

        user = new User();
        entityManager.persistAndFlush(user);

        transaction = new Transaction();
        transaction.setAdmin(user);
        entityManager.persistAndFlush(transaction);

        item = new Item();
        entityManager.persistAndFlush(item);

        itemTransaction1 = new ItemTransaction();
        itemTransaction1.setTransaction(transaction);
        itemTransaction1.setItem(item);
        itemTransaction1.setBoughtQty(10);
        itemTransaction1.setPrice(new Long(1000));
        entityManager.persistAndFlush(itemTransaction1);

        itemTransaction2 = new ItemTransaction();
        itemTransaction2.setTransaction(transaction);
        itemTransaction2.setItem(item);
        itemTransaction2.setBoughtQty(10);
        itemTransaction2.setPrice(new Long(1000));
        entityManager.persistAndFlush(itemTransaction2);
//
        List<ItemTransaction> list = new ArrayList<>();
        list.add(itemTransaction1);
        list.add(itemTransaction2);
        transaction.setItemTransactions(list);


    }

    @Test
    public void findByIdItemTransaction() {
//        assertEquals(itemTransaction1,
//                itemTransactionRepository.findById(
//                        itemTransaction1.getIdItemTransaction()
//                ).get());
//        assertEquals(itemTransaction2,
//                itemTransactionRepository.findById(
//                        itemTransaction2.getIdItemTransaction()
//                ).get());

    }



//    @Test
//    public void findAllByTransaction() {
//        assertNull(itemTransactionRepository.findAllByTransaction())
//        assertEquals(0,0);
//        assertEquals(0,0);
//    }

}