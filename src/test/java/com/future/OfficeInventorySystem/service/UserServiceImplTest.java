package com.future.OfficeInventorySystem.service;

import com.future.OfficeInventorySystem.model.User;
import com.future.OfficeInventorySystem.repository.UserHasItemRepository;
import com.future.OfficeInventorySystem.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class UserServiceImplTest {

    @Autowired
    UserService userService;

    @Mock
    UserRepository userRepository;

    @Mock
    UserHasItemRepository userHasItemRepository;

    private User user1;
    private User user2;

    @Before
    public void setUp() {

        user1 = new User();
        user1.setUsername("priagung");
        user1.setName("Priagung Satyagama");
        user1.setIsAdmin(true);

        user2 = new User();
        user2.setUsername("bambang");
        user2.setName("Bambang Nugroho");
        user2.setIsAdmin(false);
        user2.setSuperior(user1);

    }

    @Test
    public void createUserSuperiorNotFoundTest() {

//        Mockito.when(userRepository
//                .findById(user1.getSuperior().getIdUser())).thenReturn(


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