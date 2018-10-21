package com.future.OfficeInventorySystem.service;

import com.future.OfficeInventorySystem.exception.UserNotFoundException;
import com.future.OfficeInventorySystem.model.User;
import com.future.OfficeInventorySystem.repository.UserHasItemRepository;
import com.future.OfficeInventorySystem.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserServiceImplTest {

    @Autowired
    UserService userService;

    @Mock
    UserRepository userRepository;


    private User user1;
    private User user2;

    @Before
    public void setUp() {

        user1 = new User();
        user1.setUsername("priagung");
        user1.setName("Priagung Satyagama");
        user1.setIsAdmin(true);
        user1.setIdUser(1L);
        user1.setActive(true);

        user2 = new User();
        user2.setUsername("bambang");
        user2.setName("Bambang Nugroho");
        user2.setIsAdmin(false);
        user2.setSuperior(user1);
        user2.setIdUser(2L);
        user2.setActive(true);

    }

    @Test(expected = UserNotFoundException.class)
    public void createUserSuperiorNotFoundTest() {
//        Optional<User> stub = Optional.of(user1);
        Mockito.when(userRepository.findById(any()))
                .thenReturn(Optional.empty());
//        Mockito.when(stub.orElseThrow(any()))
//                .thenThrow(UserNotFoundException.class);

        userService.createUser(user2);

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