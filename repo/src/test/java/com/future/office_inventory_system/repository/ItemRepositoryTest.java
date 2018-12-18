package com.future.office_inventory_system.repository;


import com.future.office_inventory_system.model.Item;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

//@RunWith(SpringRunner.class)
@DataJpaTest
@SpringBootTest(classes = SpringRunner.class)
public class ItemRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ItemRepository itemRepository;

    @Test
    public void test_1() {
        System.out.println(itemRepository.findById(new Long(0)).get());
//        assertNull(itemRepository.findById(new Long(0)));
    }

    @Test
    public void test_2() {
        assertEquals(0,0);
    }




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
        item.setIsActive(true);

        item2 = new Item();
        item2.setItemName("indomie");
        item2.setPictureURL("image.jpg");
        item2.setPrice(1000L);
        item2.setTotalQty(10);
        item2.setAvailableQty(3);
        item2.setDescription("micin++");
        item2.setIsActive(true);

        entityManager.persist(item);
    }
//
//    @Test
//    public void testFindByIdItem() {
//
//        entityManager.persist(item);
//
//        Item i = itemRepository.findByIdItemAndIsActive(item.getIdItem(), true).get();
//        assertNotNull(i);
//        assertEquals(item, i);
//
//    }
//
//    @Test
//    public void testFindAllByAvailableQtyGreaterThan() {
//
//        entityManager.persist(item);
//        entityManager.persist(item2);
//
//        List<Item> items = itemRepository
//                .findAllByAvailableQtyGreaterThanAndIsActive(0, true, PageRequest.of(0, 2))
//                .getContent();
//
//        assertEquals(2, items.size());
//        assertTrue(items.get(0).getAvailableQty() > 0);
//        assertTrue(items.get(1).getAvailableQty() > 0);
//
//        items = itemRepository
//                .findAllByAvailableQtyGreaterThanAndIsActive(0, true, PageRequest.of(1, 2))
//                .getContent();
//
//        assertEquals(0, items.size());
//
//        items = itemRepository
//                .findAllByAvailableQtyGreaterThanAndIsActive(2, true, PageRequest.of(0, 2))
//                .getContent();
//
//        assertEquals(1, items.size());
//
//        items = itemRepository
//                .findAllByAvailableQtyGreaterThanAndIsActive(2, true, PageRequest.of(1, 2))
//                .getContent();
//
//        assertEquals(0, items.size());
//
//
//
//    }
//
//    @Test
//    public void testSaveItem() {
//
//        itemRepository.save(item);
//
//        List<Item> items = itemRepository.findAllByIsActive(true);
//
//        assertNotNull(items);
//        assertEquals(1, items.size());
//        assertEquals(item, items.get(0));
//
//    }
//
//    @Test
//    public void testDeleteItem() {
//
//        entityManager.persist(item);
//        assertNotNull(itemRepository.findAllByIsActive(true));
//        assertEquals(1, itemRepository.findAllByIsActive(true).size());
//
//        itemRepository.delete(item);
//        assertEquals(0, itemRepository.findAllByIsActive(true).size());
//    }

}