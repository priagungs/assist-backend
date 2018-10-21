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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@DataJpaTest
@RunWith(SpringRunner.class)
public class UserHasItemTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserHasItemRepository userHasItemRepository;

    private User user;
    private Item itemSatu;
    private Item itemDua;
    private UserHasItem userHasItemSatu;
    private UserHasItem userHasItemDua;

    @Before
    public void setUp() throws Exception {
        user = new User();
//        user.setIdUser(Long.valueOf(16516001));
//        user.setUsername("shintaayuck");
//        user.setPassword("hashedpassword");
//        user.setName("Shinta Ayu C K");
//        user.setPicture("ini link foto");
//        user.setRole("UX Researcher");
//        user.setDivision("Technology");
//        user.setSuperior(null);
//        user.setIsAdmin(false);
        entityManager.persist(user);

        itemSatu = new Item();
//        itemSatu.setIdItem(Long.valueOf(000001));
//        itemSatu.setItemName("indomie");
//        itemSatu.setPicture("image.jpg");
//        itemSatu.setPrice(1000);
//        itemSatu.setTotalQty(10);
//        itemSatu.setAvailableQty(2);
//        itemSatu.setDescription("micin");
        entityManager.persist(itemSatu);

        itemDua = new Item();
//        itemDua.setIdItem(Long.valueOf(000002));
//        itemDua.setItemName("popmie");
//        itemDua.setPicture("image.jpg");
//        itemDua.setPrice(1000);
//        itemDua.setTotalQty(5);
//        itemDua.setAvailableQty(3);
//        itemDua.setDescription("micin instan");
        entityManager.persist(itemDua);


        userHasItemSatu = new UserHasItem();
        userHasItemSatu.setUser(user);
        userHasItemSatu.setItem(itemSatu);
        userHasItemSatu.setHasQty(2);

        entityManager.persist(userHasItemSatu);

        userHasItemDua = new UserHasItem();
        userHasItemDua.setUser(user);
        userHasItemDua.setItem(itemDua);
        userHasItemDua.setHasQty(2);

        entityManager.persist(userHasItemDua);

    }


    @Test
    public void findByIdUserHasItem() {
        assertNotNull(userHasItemRepository.findById(
                userHasItemSatu.getIdUserHasItem()
                ).get()
        );
        assertNotNull(userHasItemRepository.findById(
                userHasItemDua.getIdUserHasItem()
                ).get()
        );
        assertNull(userHasItemRepository.findById(Long.valueOf(111111)).get());
        assertEquals(userHasItemSatu,userHasItemRepository.findById(userHasItemSatu.getIdUserHasItem()).get());

    }

    @Test
    public void findAllByUser() {

        Pageable page = PageRequest.of(0,2);

        List<UserHasItem> userHasItems = userHasItemRepository
                .findAllByUser(user,page)
                .getContent();

        assertNotNull(userHasItems);
        assertEquals(2,userHasItems.size());
        assertEquals(userHasItemSatu,userHasItems.get(0));

    }

    @Test
    public void findAllByItem() {

        Pageable page = PageRequest.of(0,2);

        List<UserHasItem> userHasItems = userHasItemRepository
                .findAllByItem(itemSatu,page)
                .getContent();

        assertNotNull(userHasItems);
        assertEquals(1,userHasItems.size());
        assertEquals(userHasItemSatu,userHasItems.get(0));

    }

    @Test
    public void testSaveUserHasItem(){
        userHasItemRepository.save(userHasItemSatu);
        userHasItemRepository.save(userHasItemDua);

        List<UserHasItem> userHasItems = userHasItemRepository.findAll();

        assertNotNull(userHasItems);
        assertEquals(2,userHasItems.size());
        assertEquals(userHasItemSatu,userHasItems.get(0));
        assertEquals(userHasItemDua,userHasItems.get(1));

    }

    @Test
    public void testDeleteUserHasItem(){

        assertNotNull(userHasItemRepository.findAll());

        userHasItemRepository.delete(userHasItemSatu);
        assertEquals(1,userHasItemRepository.findAll().size());

        userHasItemRepository.delete(userHasItemDua);
        assertEquals(0,userHasItemRepository.findAll().size());

    }
}