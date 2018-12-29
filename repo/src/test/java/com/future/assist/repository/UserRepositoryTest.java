package com.future.assist.repository;

import com.future.assist.model.entity_model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {
    
    @Autowired
    private TestEntityManager entityManager;
    
    @Autowired
    private UserRepository userRepository;
    
    private User user1;
    private User user2;
    private User user3;
    private User user4;
    
    @Before
    public void setUp() throws Exception {
        user1 = new User();
        user1.setUsername("shintaayuck");
        user1.setPassword("hashedpassword");
        user1.setName("Shinta Ayu C K");
        user1.setPictureURL("ini link foto");
        user1.setRole("UX Researcher");
        user1.setDivision("Technology");
        user1.setIsActive(true);
        user1.setSuperior(null);
        user1.setIsAdmin(true);
        
        user2 = new User();
        user2.setUsername("rickykennedy25");
        user2.setPassword("hashedpassword");
        user2.setName("Ricky Kennedy");
        user2.setPictureURL("ini link foto");
        user2.setRole("System Analyst");
        user2.setDivision("Technology");
        user2.setIsActive(true);
        user2.setSuperior(null);
        user2.setIsAdmin(true);
        
        user3 = new User();
        user3.setUsername("priagungs");
        user3.setPassword("hashedpassword");
        user3.setName("Priagung Satyagama");
        user3.setPictureURL("ini link foto");
        user3.setRole("Data Scientist");
        user3.setDivision("Technology");
        user3.setIsActive(true);
        user3.setSuperior(null);
        user3.setIsAdmin(false);
        
        user4 = new User();
        user4.setUsername("dummy");
        user4.setPassword("hashedpassword");
        user4.setName("Dummy User");
        user4.setPictureURL("ini link foto");
        user4.setRole("Data Scientist");
        user4.setDivision("Technology");
        user4.setSuperior(null);
        user4.setIsAdmin(false);
        user4.setIsActive(false);
        
        user3.setSuperior(user1);
        user4.setSuperior(user1);
        
        List<User> subordinates = new ArrayList<>();
        subordinates.add(user3);
        subordinates.add(user4);
        user1.setSubordinates(subordinates);
        
        entityManager.persist(user1);
        entityManager.persist(user2);
        entityManager.persist(user3);
        entityManager.persist(user4);
        entityManager.flush();
    }
    
    
    @Test
    public void findByUsernameAndIsActive() {
        
        assertEquals(Optional.empty(), userRepository.findByUsernameAndIsActive("abcdef",true));
        assertEquals(Optional.empty(), userRepository.findByUsernameAndIsActive(user4.getUsername(), true));
        assertEquals(Optional.empty(), userRepository.findByUsernameAndIsActive(user1.getUsername(), false));
        assertEquals(Optional.empty(), userRepository.findByUsernameAndIsActive("shinta", true));
        
        assertEquals(Optional.of(user1), userRepository.findByUsernameAndIsActive("shintaayuck", true));
        assertEquals(Optional.of(user4), userRepository.findByUsernameAndIsActive(user4.getUsername(), user4.getIsActive()));
        
    }
    
    @Test
    public void findAllBySuperiorAndIsActive() {
        List<User> subordinates = new ArrayList<>();
        subordinates.add(user3);
        subordinates.add(user4);
        
        assertNotNull(userRepository
            .findAllBySuperiorAndIsActive(user1, true, PageRequest.of(0, 2))
            .getContent());
        for (int i = 0; i < userRepository.findAllBySuperiorAndIsActive(user1, true, PageRequest.of(0,2)).getTotalElements(); i++) {
            assertEquals(subordinates.get(i), userRepository
                .findAllBySuperiorAndIsActive(user1, true, PageRequest.of(0, 2))
                .getContent().get(i));
        }
    }
    
    @Test
    public void findByIdUserAndIsActive() {
        
        assertEquals(false, userRepository.findByIdUserAndIsActive(new Long(1111111), true).isPresent());
        assertEquals(user1, userRepository.findByIdUserAndIsActive(user1.getIdUser(),true).get());
        assertEquals(user2, userRepository.findByIdUserAndIsActive(user2.getIdUser(), true).get());
        assertNotEquals(Optional.empty(), userRepository.findByIdUserAndIsActive(user1.getIdUser(), true).get());
        
    }
    
    @Test
    public void findAllByIsActive() {
        
        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);
        users.add(user3);
        
        assertNotNull(userRepository.findAllByIsActive(true, PageRequest.of(0,100)));
        assertEquals(user4, userRepository.findAllByIsActive(false, PageRequest.of(0,1)).getContent().get(0));
        assertEquals(users, userRepository.findAllByIsActive(true, PageRequest.of(0, 3)).getContent());
        
    }
    
    @Test
    public void findByNameIgnoreCaseContainingAndIsActive() {
        
        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user3);
        
        assertEquals(0, userRepository.findByNameIgnoreCaseContainingAndIsActive("x", true, PageRequest.of(0,3)).getTotalElements());
        for (int i = 0; i < userRepository.findByNameIgnoreCaseContainingAndIsActive("a", true, PageRequest.of(0,8)).getTotalElements(); i++) {
            assertEquals(users.get(i), userRepository.findByNameIgnoreCaseContainingAndIsActive("a", true, PageRequest.of(0,8))
                .getContent().get(i));
        }
        for (int i = 0; i < userRepository.findByNameIgnoreCaseContainingAndIsActive("A", true, PageRequest.of(0,8)).getTotalElements(); i++) {
            assertEquals(users.get(i), userRepository.findByNameIgnoreCaseContainingAndIsActive("A", true, PageRequest.of(0,8))
                .getContent().get(i));
        }
        
    }
    
    @Test
    public void saveUser() {
        
        assertEquals("Shinta Ayu C K", userRepository.findByIdUserAndIsActive(user1.getIdUser(),true).get().getName());
        
        user1.setName("Shinta");
        userRepository.save(user1);
        
        assertNotNull(userRepository.findAll());
        assertEquals("Shinta", userRepository.findByIdUserAndIsActive(user1.getIdUser(),true).get().getName());
    }
    
    @Test
    public void deleteUser() {
        
        int count = userRepository.findAll().size();
        assertNotNull(userRepository.findAll());
        
        user3.setSuperior(null);
        user4.setSuperior(null);
        
        userRepository.save(user3);
        userRepository.save(user4);
        userRepository.delete(user1);
        assertEquals(count-1, userRepository.findAll().size());
        
    }
    
}

