package com.future.assist.repository;

import com.future.assist.model.entity_model.Item;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;


@DataJpaTest
@RunWith(SpringRunner.class)
public class ItemRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ItemRepository itemRepository;

    private Item item;
    private Item item2;

    @Before
    public void setUp() throws Exception {

        item = new Item();
        item.setItemName("indomie");
        item.setPictureURL("image.jpg");
        item.setPrice(1000L);
        item.setTotalQty(10);
        item.setAvailableQty(5);
        item.setDescription("micin");
        item.setIsActive(true);

        item2 = new Item();
        item2.setItemName("popmie");
        item2.setPictureURL("image.jpg");
        item2.setPrice(2000L);
        item2.setTotalQty(5);
        item2.setAvailableQty(3);
        item2.setDescription("micin++");
        item2.setIsActive(true);
    }

    @Test
    public void testFindByItemNameIgnoreCaseAndIsActive() {
        entityManager.persist(item);


        Optional<Item> itemFounded = itemRepository.findByItemNameIgnoreCaseAndIsActive("Indomie", false);

        assertFalse(itemFounded.isPresent());

        itemFounded = itemRepository.findByItemNameIgnoreCaseAndIsActive("Indomie", true);

        assertTrue(itemFounded.isPresent());
        assertNotNull(itemFounded.get());

        assertEquals(itemFounded.get().getItemName(), "indomie");
        assertEquals(itemFounded.get().getIsActive(), true);


    }

    @Test
    public void testFindAllByAvailableQtyGreaterThanAndIsActive() {
        entityManager.persist(item);
        entityManager.persist(item2);

        List<Item> itemsFounded = itemRepository.findAllByAvailableQtyGreaterThanAndIsActive(
                2, true, PageRequest.of(0, 2)).getContent();

        assertEquals(2, itemsFounded.size());
        assertTrue(itemsFounded.get(0).getTotalQty() > 2);
        assertTrue(itemsFounded.get(1).getTotalQty() > 2);

        itemsFounded = itemRepository.findAllByAvailableQtyGreaterThanAndIsActive(
                4, true, PageRequest.of(0, 2)).getContent();

        assertEquals(1, itemsFounded.size());
        assertTrue(itemsFounded.get(0).getTotalQty() > 4);

        itemsFounded = itemRepository.findAllByAvailableQtyGreaterThanAndIsActive(
                5, true, PageRequest.of(0, 2)).getContent();

        assertEquals(0, itemsFounded.size());

    }

    @Test
    public void testFindAllByIsActive() {

        List<Item> itemsFounded = itemRepository.findAllByIsActive(true, PageRequest.of(0, 2)).getContent();

        assertTrue(itemsFounded.size() == 0);

        entityManager.persist(item);
        entityManager.persist(item2);

        itemsFounded = itemRepository.findAllByIsActive(true, PageRequest.of(0, 2)).getContent();

        System.out.println(itemsFounded.size());
        assertTrue(itemsFounded.size() == 2);

    }

    @Test
    public void testFindByIdItemAndIsActive() {
        Optional<Item> itemsFounded = itemRepository.findByIdItemAndIsActive(item.getIdItem(), true);

        assertFalse(itemsFounded.isPresent());

        entityManager.persist(item);

        itemsFounded = itemRepository.findByIdItemAndIsActive(item.getIdItem(), true);

        assertTrue(itemsFounded.isPresent());
        assertEquals(itemsFounded.get().getIdItem(), item.getIdItem());

    }

    @Test
    public void testFindByItemNameIgnoreCaseContainingAndIsActive() {

        List<Item> itemsFounded = itemRepository.findByItemNameIgnoreCaseContainingAndIsActive(
                "mie", true, PageRequest.of(0, 2)
        ).getContent();

        assertEquals(itemsFounded.size(), 0);

        entityManager.persist(item);
        entityManager.persist(item2);

        itemsFounded = itemRepository.findByItemNameIgnoreCaseContainingAndIsActive(
                "mie", true, PageRequest.of(0, 2)
        ).getContent();

        assertNotNull(itemsFounded);
        assertEquals(itemsFounded.size(), 2);

    }

    @Test
    public void testFindByItemNameIgnoreCaseContainingAndAvailableQtyGreaterThanAndIsActive() {
        List<Item> itemsFounded =
                itemRepository.findByItemNameIgnoreCaseContainingAndAvailableQtyGreaterThanAndIsActive(
                        "mie", 2, true, PageRequest.of(0, 2)
                ).getContent();

        assertEquals(itemsFounded.size(), 0);

        entityManager.persist(item);
        entityManager.persist(item2);

        itemsFounded =
                itemRepository.findByItemNameIgnoreCaseContainingAndAvailableQtyGreaterThanAndIsActive(
                        "mie", 2, true, PageRequest.of(0, 2)
                ).getContent();


        assertNotNull(itemsFounded);
        assertEquals(itemsFounded.size(), 2);
    }

    @Test
    public void testSaveItem() {

        itemRepository.save(item);

        List<Item> items = itemRepository.findAllByIsActive(
                true, PageRequest.of(0, 2)).getContent();

        assertNotNull(items);
        assertEquals(1, items.size());
        assertEquals(item, items.get(0));

    }

    @Test
    public void testDeleteItem() {

        entityManager.persist(item);
        entityManager.flush();

        assertNotNull(itemRepository.findAllByIsActive(
                true, PageRequest.of(0, 2))
                .getContent());
        assertEquals(1, itemRepository.findAllByIsActive(
                true, PageRequest.of(0, 2))
                .getContent()
                .size());

        itemRepository.delete(item);
        assertEquals(0, itemRepository.findAllByIsActive(
                true, PageRequest.of(0, 2))
                .getContent()
                .size());

    }
}