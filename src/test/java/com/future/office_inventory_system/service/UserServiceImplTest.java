package com.future.office_inventory_system.service;


import com.future.office_inventory_system.exception.NotFoundException;
import com.future.office_inventory_system.model.User;
import com.future.office_inventory_system.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserServiceImplTest {

    @Autowired
    UserService userService;

    @MockBean
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
        user1.setIsActive(true);

        user2 = new User();
        user2.setUsername("bambang");
        user2.setName("Bambang Nugroho");
        user2.setIsAdmin(false);
        user2.setSuperior(user1);
        user2.setIdUser(2L);
        user2.setIsActive(true);

    }

    @Test(expected = NotFoundException.class)
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