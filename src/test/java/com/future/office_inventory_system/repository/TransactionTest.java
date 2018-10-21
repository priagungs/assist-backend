package com.future.office_inventory_system.repository;

import com.future.office_inventory_system.model.Transaction;
import com.future.office_inventory_system.model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

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
        Long id = Long.valueOf(transaction1.getIdTransaction());
        assertEquals(transaction1, transactionRepository.findById(id).get());
    }

    @Test
    public void findAllByAdmin() {
        Pageable page =  PageRequest.of(0,3);

        List<Transaction> transactions = transactionRepository
                .findAllByAdmin(admin,page)
                .getContent();

        assertNotNull(transactions);
        assertEquals(3, transactions.size());
        assertEquals(transaction1,transactions.get(0));

    }

    @Test
    public void testSaveTransaction(){
        transactionRepository.save(transaction1);
        transactionRepository.save(transaction2);
        transactionRepository.save(transaction3);

        assertNotNull(transactionRepository.findAll());
        assertEquals(3,transactionRepository.findAll().size());
        assertEquals(transaction1,transactionRepository.findAll().get(0));
        assertEquals(transaction2,transactionRepository.findAll().get(1));
        assertEquals(transaction3,transactionRepository.findAll().get(2));

    }

    @Test
    public void testDeleteTransaction(){

        assertNotNull(transactionRepository.findAll());

        transactionRepository.delete(transaction1);
        assertEquals(2,transactionRepository.findAll().size());

        transactionRepository.delete(transaction2);
        assertEquals(1,transactionRepository.findAll().size());

        transactionRepository.delete(transaction3);
        assertEquals(0,transactionRepository.findAll().size());

    }
}