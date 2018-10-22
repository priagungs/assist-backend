package com.future.office_inventory_system.service;


import com.future.office_inventory_system.exception.NotFoundException;
import com.future.office_inventory_system.model.User;
import com.future.office_inventory_system.repository.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
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
    private User user3;

    @Before
    public void setUp() {

        user1 = new User();
        user1.setUsername("priagung");
        user1.setName("Priagung Satyagama");
        user1.setIsAdmin(true);
        user1.setIsActive(true);

        user2 = new User();
        user2.setUsername("bambang");
        user2.setName("Bambang Nugroho");
        user2.setIsAdmin(false);
        user2.setSuperior(user1);
        user2.setIsActive(true);
        user2.setPictureURL("picture.img");
        user2.setPassword("asdfasdf");
        user2.setDivision("Engineering");
        user2.setRole("Software Engineer");

    }

    @Test(expected = NotFoundException.class)
    public void createUserSuperiorNotFoundTest() {
        Mockito.when(userRepository.findByIdUserAndIsActive(user2.getSuperior().getIdUser(), true))
                .thenReturn(Optional.empty());

        userService.createUser(user2);
    }

    @Test
    public void createUserSuccessTest() {
        Mockito.when(userRepository.save(user2)).thenReturn(user2);
        Mockito.when(userRepository.findByIdUserAndIsActive(user2.getSuperior().getIdUser(), true))
                .thenReturn(Optional.of(user1));

        Assert.assertEquals(user2, userService.createUser(user2));
    }

    @Test(expected = NotFoundException.class)
    public void updateUserNotFoundTest() {
        Mockito.when(userRepository.findByIdUserAndIsActive(user2.getIdUser(), true))
                .thenReturn(Optional.empty());

        userService.updateUser(user2);

    }

    @Test(expected = NotFoundException.class)
    public void updateUserSuperiorNotFoundTest() {
        Mockito.when(userRepository.findByIdUserAndIsActive(user2.getIdUser(), true))
                .thenReturn(Optional.of(user2));
        Mockito.when(userRepository.findByIdUserAndIsActive(user2.getSuperior().getIdUser(), true))
                .thenReturn(Optional.empty());

        userService.updateUser(user2);
    }

    @Test
    public void updateUserSuccessTest() {

        Mockito.when(userRepository.findByIdUserAndIsActive(user2.getIdUser(), true))
                .thenReturn(Optional.of(user2));
        Mockito.when(userRepository.findByIdUserAndIsActive(user2.getSuperior().getIdUser(), true))
                .thenReturn(Optional.of(user1));

        Mockito.when(userRepository.save(any())).thenReturn(user2);

        User result = userService.updateUser(user2);
        Assert.assertEquals("Bambang Nugroho", result.getName());

    }

    @Test
    public void readAllUser() {
        PageImpl contents = new PageImpl(new ArrayList());

        Mockito.when(userRepository.findAllByIsActive(true, PageRequest.of(0, Integer.MAX_VALUE)))
                .thenReturn(contents);

        Assert.assertEquals(contents, userService.readAllUsers(PageRequest.of(0, Integer.MAX_VALUE)));

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