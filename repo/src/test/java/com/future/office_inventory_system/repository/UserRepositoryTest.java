package com.future.office_inventory_system.repository;

import com.future.office_inventory_system.model.entity_model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;


import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @Before
    public void initUserRepositoryTest() {
        user = new User();
        user.setIsActive(true);
        user.setName("user");
        user.setPasswordWithoutEncode("kennedy");
        user.setUsername("kennedy");
        user.setIsAdmin(true);

        entityManager.persist(user);
        entityManager.flush();
    }

    @Test
    public void findByUsernameSuccess() {

        Optional<User> founder = userRepository.findByUsername(user.getUsername());

        assertEquals(founder.get().getUsername(),user.getUsername());
    }

    @Test
    public void findByUsernameNotFound() {

        Optional<User> founder = userRepository.findByUsername("kennedy");

        assertFalse(founder.isPresent());
    }
}