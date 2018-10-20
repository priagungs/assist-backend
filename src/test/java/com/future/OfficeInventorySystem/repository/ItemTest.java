package com.future.OfficeInventorySystem.repository;

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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    private Item item2;

    @Before
    public void setUp() {

        item = new Item();
        item.setItemName("indomie");
        item.setPictureURL("image.jpg");
        item.setPrice(1000L);
        item.setTotalQty(10);
        item.setAvailableQty(1);
        item.setDescription("micin");

        item2 = new Item();
        item2.setItemName("indomie");
        item2.setPictureURL("image.jpg");
        item2.setPrice(1000L);
        item2.setTotalQty(10);
        item2.setAvailableQty(3);
        item2.setDescription("micin++");
    }

    @Test
    public void testFindByIdItem() {

        entityManager.persist(item);

        Item i = itemRepository.findById(item.getIdItem()).get();
        assertNotNull(i);
        assertEquals(item, i);

    }

    @Test
    public void testFindAllByItemName() {

        entityManager.persist(item);
        entityManager.persist(item2);
        Pageable page = new PageRequest(1, 1);

        List<Item> items = itemRepository
                .findAllByItemName("indomie", page)
                .getContent();
        assertNotNull(items);
        assertEquals(1, items.size());
        assertEquals("indomie", items.get(0).getItemName());

        page = new PageRequest(0, 2);
        items = itemRepository
                .findAllByItemName("indomie", page)
                .getContent();
        assertEquals(2, items.size());
        assertEquals("indomie", items.get(0).getItemName());
        assertEquals("indomie", items.get(1).getItemName());

    }

    @Test
    public void testFindAllByAvailableQtyGreaterThan() {

        entityManager.persist(item);
        entityManager.persist(item2);

        List<Item> items = itemRepository
                .findAllByAvailableQtyGreaterThan(0, new PageRequest(0, 2))
                .getContent();

        assertEquals(2, items.size());
        assertTrue(items.get(0).getAvailableQty() > 0);
        assertTrue(items.get(1).getAvailableQty() > 0);

        items = itemRepository
                .findAllByAvailableQtyGreaterThan(0, new PageRequest(1, 2))
                .getContent();

        assertEquals(0, items.size());

        items = itemRepository
                .findAllByAvailableQtyGreaterThan(2, new PageRequest(0, 2))
                .getContent();

        assertEquals(1, items.size());

        items = itemRepository
                .findAllByAvailableQtyGreaterThan(2, new PageRequest(1, 2))
                .getContent();

        assertEquals(0, items.size());



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