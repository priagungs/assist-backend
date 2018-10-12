package com.future.OfficeInventorySystem.model;

import com.future.OfficeInventorySystem.model.Item;
import com.future.OfficeInventorySystem.repository.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@DataJpaTest
@RunWith(SpringRunner.class)
public class ItemTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ItemRepository itemRepository;

    private Item item;

    @Before
    public void setUp() {

        item = new Item();
        item.setItemName("indomie");
        item.setPicture("image.jpg");
        item.setPrice(1000);
        item.setTotalQty(10);
        item.setAvailableQty(1);
        item.setDescription("micin");

    }

    @Test
    public void testFindByIdItem() {
        itemRepository.save(item);
        assertNotNull(itemRepository.findAllByItemName("indomie"));
    }

}