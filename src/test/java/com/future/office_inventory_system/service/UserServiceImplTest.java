package com.future.office_inventory_system.service;


import com.future.office_inventory_system.exception.NotFoundException;
import com.future.office_inventory_system.model.User;
import com.future.office_inventory_system.repository.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
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

    @MockBean
    UserHasItemService userHasItemService;


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
        user1.setHasItems(new ArrayList<>());


        user2 = new User();
        List<User> subordinates = new ArrayList<>();
        subordinates.add(user2);
        user1.setSubordinates(subordinates);

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
    public void readUserByIdUserSuccessTest() {
        Mockito.when(userRepository.findByIdUserAndIsActive(user2.getIdUser(), true))
                .thenReturn(Optional.of(user2));

        Assert.assertEquals(user2, userService.readUserByIdUser(user2.getIdUser()));
    }

    @Test(expected = NotFoundException.class)
    public void readUserByIdUserNotFoundTest() {
        Mockito.when(userRepository.findByIdUserAndIsActive(user2.getIdUser(), true))
                .thenReturn(Optional.empty());

        userService.readUserByIdUser(user2.getIdUser());
    }

    @Test(expected = NotFoundException.class)
    public void readAllUsersByIdSuperiorNotFoundTest() {
        Mockito.when(userRepository.findByIdUserAndIsActive(user2.getSuperior().getIdUser(), true))
                .thenReturn(Optional.empty());

        userService.readAllUsersByIdSuperior(user2.getSuperior().getIdUser(),
                PageRequest.of(0, Integer.MAX_VALUE));
    }

    @Test
    public void readAllUsersByIdSuperiorSuccessTest() {
        Mockito.when(userRepository.findByIdUserAndIsActive(user2.getSuperior().getIdUser(), true))
                .thenReturn(Optional.of(user1));

        PageImpl contents = new PageImpl(new ArrayList());

        Mockito.when(userRepository.findAllBySuperior(user1, PageRequest.of(0, Integer.MAX_VALUE)))
                .thenReturn(contents);

        Assert.assertEquals(contents, userService.readAllUsersByIdSuperior(user2.getSuperior().getIdUser(),
                PageRequest.of(0, Integer.MAX_VALUE)));
    }

    @Test
    public void readAllUsersByIsAdminTest() {
        Mockito.when(userRepository.findAllByIsAdminAndIsActive(true, true,
                PageRequest.of(0, Integer.MAX_VALUE)))
                .thenReturn(Page.empty());

        Assert.assertEquals(Page.empty(), userService.readAllUsersByIsAdmin(true,
                PageRequest.of(0, Integer.MAX_VALUE)));
    }

    @Test(expected = NotFoundException.class)
    public void readUserByUsernameNotFoundTest() {
        Mockito.when(userRepository.findByUsernameAndIsActive(user1.getUsername(), true))
                .thenReturn(Optional.empty());

        userService.readUserByUsername(user1.getUsername());
    }

    @Test
    public void readUserByUsernameFoundTest() {
        Mockito.when(userRepository.findByUsernameAndIsActive(user1.getUsername(), true))
                .thenReturn(Optional.of(user1));

        userService.readUserByUsername(user1.getUsername());
    }

    @Test(expected = NotFoundException.class)
    public void deleteUserNotFoundTest() {
        Mockito.when(userRepository.findByIdUserAndIsActive(user1.getIdUser(), true))
                .thenReturn(Optional.empty());

        userService.deleteUser(user1.getIdUser());
    }

    @Test
    public void deleteUserSuccess() {
        Mockito.when(userRepository.findByIdUserAndIsActive(user1.getIdUser(), true))
                .thenReturn(Optional.of(user1));
        Mockito.when(userHasItemService.deleteUserHasItem(any())).thenReturn(ResponseEntity.ok().build());

        Assert.assertEquals(ResponseEntity.ok().build(), userService.deleteUser(user1.getIdUser()));
    }
}