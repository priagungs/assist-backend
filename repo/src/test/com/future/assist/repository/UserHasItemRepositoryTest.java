package com.future.assist.repository;

import com.future.assist.model.entity_model.Item;
import com.future.assist.model.entity_model.User;
import com.future.assist.model.entity_model.UserHasItem;
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

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserHasItemRepositoryTest {
    
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
        user.setUsername("shintaayuck");
        user.setPassword("hashedpassword");
        user.setName("Shinta Ayu C K");
        user.setPictureURL("ini link foto");
        user.setRole("UX Researcher");
        user.setDivision("Technology");
        user.setSuperior(null);
        user.setIsActive(true);
        user.setIsAdmin(false);
        
        itemSatu = new Item();
        itemSatu.setItemName("indomie");
        itemSatu.setPictureURL("image.jpg");
        itemSatu.setPrice(1000L);
        itemSatu.setTotalQty(10);
        itemSatu.setAvailableQty(2);
        itemSatu.setDescription("micin");
        
        itemDua = new Item();
        itemDua.setItemName("popmie");
        itemDua.setPictureURL("image.jpg");
        itemDua.setPrice(1000L);
        itemDua.setTotalQty(5);
        itemDua.setAvailableQty(3);
        itemDua.setDescription("micin instan");
        
        userHasItemSatu = new UserHasItem();
        userHasItemSatu.setUser(user);
        userHasItemSatu.setItem(itemSatu);
        userHasItemSatu.setHasQty(2);
        
        userHasItemDua = new UserHasItem();
        userHasItemDua.setUser(user);
        userHasItemDua.setItem(itemDua);
        userHasItemDua.setHasQty(2);
    
        entityManager.persistAndFlush(user);
        entityManager.persistAndFlush(itemSatu);
        entityManager.persistAndFlush(itemDua);
        
        entityManager.persistAndFlush(userHasItemSatu);
        entityManager.persistAndFlush(userHasItemDua);
        
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
    public void findAllByUserAndItem() {
        
        List<UserHasItem> userHasItems1 = userHasItemRepository
            .findAllByUserAndItem(user, itemSatu);
        List<UserHasItem> userHasItems2 = userHasItemRepository
            .findAllByUserAndItem(user, itemDua);
        
        assertEquals(1, userHasItems1.size());
        assertEquals(userHasItemSatu, userHasItems1.get(0));
        assertEquals(1, userHasItems2.size());
        assertEquals(userHasItemDua, userHasItems2.get(0));
        
        
        
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
        assertFalse(userHasItemRepository.findById(Long.valueOf(111111)).isPresent());
        assertEquals(userHasItemSatu,userHasItemRepository.findById(userHasItemSatu.getIdUserHasItem()).get());
        
    }
    
    @Test
    public void saveUserHasItem(){
        userHasItemRepository.save(userHasItemSatu);
        userHasItemRepository.save(userHasItemDua);
        
        List<UserHasItem> userHasItems = userHasItemRepository.findAll();
        
        assertNotNull(userHasItems);
        assertEquals(2,userHasItems.size());
        assertEquals(userHasItemSatu,userHasItems.get(0));
        assertEquals(userHasItemDua,userHasItems.get(1));
        
    }
    
    @Test
    public void deleteUserHasItem(){
        
        assertNotNull(userHasItemRepository.findAll());
        
        userHasItemRepository.delete(userHasItemSatu);
        assertEquals(1,userHasItemRepository.findAll().size());
        
        userHasItemRepository.delete(userHasItemDua);
        assertEquals(0,userHasItemRepository.findAll().size());
        
    }
}