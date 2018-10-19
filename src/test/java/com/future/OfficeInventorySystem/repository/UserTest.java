package com.future.OfficeInventorySystem.repository;

import com.future.OfficeInventorySystem.model.User;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@DataJpaTest
@RunWith(SpringRunner.class)
public class UserTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private User user1;
    private User user2;
    private User user3;
    private User user4;
    private User user;

    @Before
    public void setUp() throws Exception {
        user1 = new User();
        user1.setUsername("shintaayuck");
        user1.setPassword("hashedpassword");
        user1.setName("Shinta Ayu C K");
        user1.setPicture("ini link foto");
        user1.setRole("UX Researcher");
        user1.setDivision("Technology");
        user1.setSuperior(null);
        user1.setIsAdmin(true);

        user2 = new User();
        user2.setUsername("rickykennedy25");
        user2.setPassword("hashedpassword");
        user2.setName("Ricky Kennedy");
        user2.setPicture("ini link foto");
        user2.setRole("System Analyst");
        user2.setDivision("Technology");
        user2.setSuperior(null);
        user2.setIsAdmin(true);

        user3 = new User();
        user3.setUsername("priagungs");
        user3.setPassword("hashedpassword");
        user3.setName("Priagung Satyagama");
        user3.setPicture("ini link foto");
        user3.setRole("Data Scientist");
        user3.setDivision("Technology");
        user3.setSuperior(user1);
        user3.setIsAdmin(false);

        user4 = new User();
        user4.setUsername("priagungs");
        user4.setPassword("hashedpassword");
        user4.setName("Priagung Satyagama");
        user4.setPicture("ini link foto");
        user4.setRole("Data Scientist");
        user4.setDivision("Technology");
        user4.setSuperior(user1);
        user4.setIsAdmin(false);

        List<User> subordinates = new ArrayList<>();
        subordinates.add(user3);
        subordinates.add(user4);
        user1.setSubordinates(subordinates);


        entityManager.persist(user1);
        entityManager.persist(user2);
        entityManager.persist(user3);
        entityManager.persist(user4);
    }

    @Test
    public void findByUsername() {

        assertNull(userRepository.findByUsername("abcdef"));
        assertNotNull(userRepository.findByUsername("rickykennedy25"));
        assertEquals(user2,userRepository.findByUsername("rickykennedy25"));

    }

    @Test
    public void findByIdUser() {

        assertNull(userRepository.findById(new Long(1111111)).get());
        assertEquals(user1, userRepository.findById(user1.getIdUser()).get());
        assertEquals(user2, userRepository.findById(user2.getIdUser()).get());
        assertNotNull(userRepository.findById(user1.getIdUser()).get());

    }
    @Test
    public void findAllByName() {

        List<User> userList = new ArrayList<>();
        userList.add(user3);
        userList.add(user4);

        assertEquals(0,userRepository
                .findAllByName("abcdef", new PageRequest(0, 2))
                .getContent()
                .size());
        assertNotNull(userRepository
                .findAllByName("Priagung Satyagama", new PageRequest(0, 2))
                .getContent());
        assertEquals(userList,userRepository
                .findAllByName("Priagung Satyagama", new PageRequest(0, 2))
                .getContent());

    }


    @Test
    public void findAllBySuperior() {

        List<User> employeeList = new ArrayList<>();
        employeeList.add(user3);
        employeeList.add(user4);

        assertNotNull(userRepository
                .findAllBySuperior(user1, new PageRequest(0, 2))
                .getContent());
        assertEquals(employeeList, userRepository
                .findAllBySuperior(user1, new PageRequest(0, 2))
                .getContent());

    }

    @Test
    public void findAllByIsAdmin() {
        List<User> adminList = new ArrayList<>();
        adminList.add(user1);
        adminList.add(user2);
        List<User> notAdminList = new ArrayList<>();
        notAdminList.add(user3);
        notAdminList.add(user4);

        assertEquals(adminList, userRepository
                .findAllByIsAdmin(true, new PageRequest(0, 2))
                .getContent());
        assertEquals(notAdminList, userRepository
                .findAllByIsAdmin(false, new PageRequest(0, 2))
                .getContent());

    }

    @Test
    public void saveUser() {
        userRepository.save(user1);

        List<User> users = userRepository.findAll();
        assertNotNull(users);
        assertEquals(4,users.size());
        assertEquals(user1, users.get(0));
    }

    @Test
    public void deleteUser() {

        assertNotNull(userRepository.findAll());
        assertEquals(4, userRepository.findAll().size());

        user3.setSuperior(null);
        user4.setSuperior(null);

        userRepository.save(user3);
        userRepository.save(user4);
        userRepository.delete(user1);
        assertEquals(3, userRepository.findAll().size());

    }

}