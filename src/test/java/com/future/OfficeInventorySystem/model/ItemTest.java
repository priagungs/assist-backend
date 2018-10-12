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

        entityManager.persist(item);

        Item i = itemRepository.findByIdItem(new Long(13216001));
        assertNotNull(i);
        assertEquals(item, i);

    }

    @Test
    public void testFindAllByItemName() {

        entityManager.persist(item);
        Item i = new Item();
        i.setItemName("indomie");
        entityManager.persist(i);

        List<Item> items = itemRepository.findAllByItemName("indomie");
        assertNotNull(items);
        assertEquals(2, items.size());
        assertEquals("indomie", items.get(0).getItemName());
        assertEquals("indomie", items.get(1).getItemName());

    }

    @Test
    public void testFindAllByAvailableQtyGreaterThan() {

        entityManager.persist(item);

        assertEquals(itemRepository.findAllByAvailableQtyGreaterThan(2).size(), 0);
        assertNotNull(itemRepository.findAllByAvailableQtyGreaterThan(0));

        List<Item> items = itemRepository.findAllByAvailableQtyGreaterThan(0);
        assertEquals(1, items.size());
        assertTrue(items.get(0).getAvailableQty() > 0);

    }

    @Test
    public void testSaveItem() {

        itemRepository.save(item);

        List<Item> items = itemRepository.findAll();

        assertNotNull(items);
        assertEquals(1, items.size());
        assertEquals(item, items.get(0));

    }

    @Test
    public void testDeleteItem() {

        entityManager.persist(item);
        assertNotNull(itemRepository.findAll());
        assertEquals(1, itemRepository.findAll().size());

        itemRepository.delete(item);
        assertEquals(0, itemRepository.findAll().size());

    }

}