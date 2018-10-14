package com.future.OfficeInventorySystem.repository;

import com.future.OfficeInventorySystem.model.Item;
import com.future.OfficeInventorySystem.model.User;
import com.future.OfficeInventorySystem.model.UserHasItem;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@DataJpaTest
@RunWith(SpringRunner.class)
public class UserHasItemTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserHasItemRepository userHasItemRepository;

    private User user1;
    private Item item1;
    private Item item2;
    private UserHasItem userHasItem1;
    private UserHasItem userHasItem2;

    @Before
    public void setUp() {
        user1 = new User();
        user1.setUsername("shintaayuck");
        user1.setPassword("hashedpassword");
        user1.setName("Shinta Ayu C K");
        user1.setPicture("ini link foto");
        user1.setRole("UX Researcher");
        user1.setDivision("Technology");
        user1.setSuperior(null);
        user1.setIsAdmin(false);

        item1 = new Item();
        item1.setItemName("indomie");
        item1.setPicture("image.jpg");
        item1.setPrice(1000);
        item1.setTotalQty(10);
        item1.setAvailableQty(2);
        item1.setDescription("micin");

        item2 = new Item();
        item2.setItemName("popmie");
        item2.setPicture("image.jpg");
        item2.setPrice(1000);
        item2.setTotalQty(5);
        item2.setAvailableQty(3);
        item2.setDescription("micin instan");

        userHasItem1 = new UserHasItem();
        userHasItem1.setUser(user1);
        userHasItem1.setItem(item1);
        userHasItem1.setHasQty(2);

        userHasItem2 = new UserHasItem();
        userHasItem2.setUser(user1);
        userHasItem2.setItem(item2);
        userHasItem2.setHasQty(2);

        entityManager.persist(user1);
        entityManager.persist(item1);
        entityManager.persist(item2);
        entityManager.persist(userHasItem1);
        entityManager.persist(userHasItem2);

    }


    @Test
    public void findByIdUserHasItem() {

        assertNotNull(userHasItemRepository.findByIdUserHasItem(
                userHasItem1.getIdUserHasItem()
                )
        );
        assertNotNull(userHasItemRepository.findByIdUserHasItem(
                userHasItem2.getIdUserHasItem()
                )
        );
        assertNull(userHasItemRepository.findByIdUserHasItem(Long.valueOf(111111)));
        assertEquals(userHasItem1,userHasItemRepository.findByIdUserHasItem(userHasItem1.getIdUserHasItem()));

    }

    @Test
    public void findAllByUser() {
        assertTrue(true);
    }

    @Test
    public void findAllByItem() {
        assertTrue(true);

    }

    @Test
    public void testSaveUserHasItem(){
        assertTrue(true);

    }

    @Test
    public void testDeleteUserHasItem(){
        assertTrue(true);

    }
}