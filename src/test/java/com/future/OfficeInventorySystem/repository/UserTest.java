package com.future.OfficeInventorySystem.repository;

import com.future.OfficeInventorySystem.model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
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

    @Before
    public void setUp() throws Exception {
        user1 = new User();
        user1.setUsername("shintaayuck");
        user1.setPassword("hashedpassword");
        user1.setName("Shinta Ayu C K");
        user1.setPicture("ini link foto");
        user1.setRole("UX Researcher");
        user1.setDivision("Technology");
        user1.setSuperior(user3);
        user1.setIsAdmin(true);

        user2 = new User();
        user2.setUsername("rickykennedy25");
        user2.setPassword("hashedpassword");
        user2.setName("Ricky Kennedy");
        user2.setPicture("ini link foto");
        user2.setRole("System Analyst");
        user2.setDivision("Technology");
        user1.setSuperior(user3);
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

        entityManager.persist(user1);
        entityManager.persist(user2);
        entityManager.persist(user3);

    }

    @Test
    public void findByIdUser() {
        assertNotNull(userRepository.findByIdUser(new Long(16516001)));
        assertEquals(userRepository.findByIdUser(new Long(16516001)), user1);
        assertEquals(userRepository.findByIdUser(new Long(16516002)), user2);
    }

    @Test
    public void findByUsername() {
    }

    @Test
    public void findAllByName() {
    }

    @Test
    public void findAllByRole() {
    }

    @Test
    public void findAllByDivision() {
    }

    @Test
    public void findAllBySuperior() {
        List<User> employeeList = new ArrayList<>();
        employeeList.add(user1);
        employeeList.add(user2);
//        assertEquals(employeeList, userRepository.findAllBySuperior(user3));
    }

    @Test
    public void findAllByIsAdmin() {
        List<User> adminList = new ArrayList<>();
        adminList.add(user1);
        adminList.add(user2);
        List<User> notAdminList = new ArrayList<>();
        notAdminList.add(user3);

        assertEquals(adminList, userRepository.findAllByIsAdmin(true));
        assertEquals(notAdminList, userRepository.findAllByIsAdmin(false));

    }
}