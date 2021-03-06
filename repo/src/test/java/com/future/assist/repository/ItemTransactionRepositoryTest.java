package com.future.assist.repository;

import com.future.assist.model.entity_model.Item;
import com.future.assist.model.entity_model.ItemTransaction;
import com.future.assist.model.entity_model.Transaction;
import com.future.assist.model.entity_model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ItemTransactionRepositoryTest {
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

        List<ItemTransaction> list = new ArrayList<>();
        list.add(itemTransaction1);
        list.add(itemTransaction2);
        transaction.setItemTransactions(list);
    }

    @Test
    public void findByIdItemTransaction() {
        assertEquals(itemTransaction1,
                itemTransactionRepository.findById(
                        itemTransaction1.getIdItemTransaction()
                ).get());
        assertEquals(itemTransaction2,
                itemTransactionRepository.findById(
                        itemTransaction2.getIdItemTransaction()
                ).get());
    }

    @Test
    public void findAllByTransaction() {
        List<ItemTransaction> list = itemTransactionRepository
                .findAllByTransaction(transaction,
                        PageRequest.of(0, 4))
                .getContent();
        assertNotNull(list);
        assertEquals(2, list.size());
        assertEquals(itemTransaction1, list.get(0));
    }

    @Test
    public void saveItemTransaction() {
        itemTransactionRepository.save(itemTransaction1);
        itemTransactionRepository.save(itemTransaction2);

        List<ItemTransaction> itemTransactions = itemTransactionRepository
                .findAll();

        assertNotNull(itemTransactions);
        assertEquals(2, itemTransactions.size());
        assertEquals(itemTransaction1, itemTransactions.get(0));
        assertEquals(itemTransaction2, itemTransactions.get(1));
    }

    @Test
    public void deleteItemTransaction() {
        assertNotNull(itemTransactionRepository.findAll());
        assertEquals(2, itemTransactionRepository.findAll().size());

        itemTransactionRepository.deleteById(itemTransaction1.getIdItemTransaction());
        assertEquals(1, itemTransactionRepository.findAll().size());

        itemTransactionRepository.delete(itemTransaction2);
        assertEquals(0, itemTransactionRepository.findAll().size());
    }
}