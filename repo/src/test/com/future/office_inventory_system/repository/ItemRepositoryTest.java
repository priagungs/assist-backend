package com.future.office_inventory_system.repository;

import com.future.office_inventory_system.model.Item;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@DataJpaTest
@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ItemRepositoryTest {
    
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
        item.setIsActive(true);
        
        item2 = new Item();
        item2.setItemName("indomie");
        item2.setPictureURL("image.jpg");
        item2.setPrice(1000L);
        item2.setTotalQty(10);
        item2.setAvailableQty(3);
        item2.setDescription("micin++");
        item2.setIsActive(true);
    }
    
    @Test
    public void testFindByIdItemAndIsActive() {
        
        entityManager.persist(item);
        
        Item i = itemRepository.findByIdItemAndIsActive(item.getIdItem(), true).get();
        assertNotNull(i);
        assertEquals(item, i);
        
    }
}