package com.future.office_inventory_system.repository;

import com.future.office_inventory_system.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;


import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class) //kalau pakai initialization error
@DataJpaTest
//@SpringBootTest //kalau pakai ini TestEntitymanagernya null mulu
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void test1() {
        User ricky = new User();

        ricky.setIsActive(true);
        ricky.setName("ricky");
        ricky.setPasswordWithoutEncode("kennedy");
        ricky.setUsername("kennedy");
        ricky.setIsAdmin(true);

        entityManager.persist(ricky);
        entityManager.flush();

        Optional<User> founder = userRepository.findByUsername(ricky.getUsername());

        assertEquals(founder.get().getUsername(),ricky.getUsername());
    }
}