package com.future.OfficeInventorySystem.service;

import com.future.OfficeInventorySystem.model.User;
import com.future.OfficeInventorySystem.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserServiceImplTest {

    @Autowired
    private UserServiceImpl userService;

    @MockBean
    private UserRepository userRepository;

    User user1;
    User user2;
    User user3;

    @Before
    public void setUp() {
        userService.setPageRequest(new PageRequest(0, Integer.MAX_VALUE));

        user1 = new User();
        user1.setName("priagung");
        user1.setIsAdmin(true);
        user1.setUsername("priagung");

        user2 = new User();
        user2.setName("shinta");
        user2.setIsAdmin(false);
        user2.setUsername("shinta");

        user3 = new User();
        user3.setName("ricky");
        user3.setIsAdmin(false);
        user3.setUsername("ricky");

    }

    @Test
    public void createUser() {
        Mockito.when(userRepository.findByIdUser(user1.getIdUser()))
                .thenReturn(user1);
        assertFalse(userService.createUser(user1));

        Mockito.when(userRepository.findByIdUser(user2.getSuperior().getIdUser()))
                .thenReturn(null);
        assertFalse(userService.createUser(user2));



    }

    @Test
    public void updateUser() {
    }

    @Test
    public void readAllUser() {
    }

    @Test
    public void readUserByIdUser() {
    }

    @Test
    public void readUserByIdSuperior() {
    }

    @Test
    public void readUserByIsAdmin() {
    }

    @Test
    public void readUserByUsername() {
    }

    @Test
    public void deleteUser() {
    }
}