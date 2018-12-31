package com.future.assist.service.service_impl;

import com.future.assist.exception.ConflictException;
import com.future.assist.exception.InvalidValueException;
import com.future.assist.exception.NotFoundException;
import com.future.assist.model.entity_model.User;
import com.future.assist.repository.UserRepository;
import com.future.assist.service.service_interface.UserHasItemService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserServiceImplTest {

    @Autowired
    private UserServiceImpl userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserHasItemService userHasItemService;

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

        user3 = new User();
        user3.setUsername("kennedy");
        user3.setName("Ricky Kennedy");
        user3.setIsAdmin(true);
        user3.setIsActive(true);
        user3.setHasItems(new ArrayList<>());

    }

    @Test (expected = NotFoundException.class)
    public void createUserSuperiorNotFoundTest() {
        Mockito.when(userRepository.findByIdUserAndIsActive(user2.getSuperior().getIdUser(), true))
                .thenReturn(Optional.empty());

        userService.createUser(user2);
    }

    @Test (expected = ConflictException.class)
    public void createUserUsernameConflictTest() {
        Mockito.when(userRepository.findByIdUserAndIsActive(user2.getSuperior().getIdUser(), true))
                .thenReturn(Optional.of(user1));
        Mockito.when(userRepository.findByUsernameAndIsActive(user2.getUsername(), true))
                .thenReturn(Optional.of(user2));

        userService.createUser(user2);
    }

    @Test
    public void creteUserUsernameSuccessTest() {
        Mockito.when(userRepository.save(user2)).thenReturn(user2);

        Mockito.when(userRepository.findByIdUserAndIsActive(user2.getSuperior().getIdUser(), true))
                .thenReturn(Optional.of(user1));

        Mockito.when(userRepository.findByUsernameAndIsActive(user2.getUsername(), true))
                .thenReturn(Optional.empty());

        assertEquals(user2,userService.createUser(user2));
    }

    @Test (expected = NotFoundException.class)
    public void updateUserNotFoundUserTest() {
        Mockito.when(userRepository.findByIdUserAndIsActive(user1.getIdUser(),true))
                .thenReturn(Optional.empty());

        userService.updateUser(user1);
    }

    @Test (expected = InvalidValueException.class)
    public void updateUserCircularSuperiorTest() {


        Mockito.when(userRepository.findByIdUserAndIsActive(user2.getSuperior().getIdUser(), true))
                .thenReturn(Optional.of(user1));

        userService.updateUser(user2);
    }


    @Test (expected = NotFoundException.class)
    public void updateUserSuperiorNotFoundTest() {
        Mockito.when(userRepository.findByIdUserAndIsActive(user2.getIdUser(), true))
                .thenReturn(Optional.of(user2));
        Mockito.when(userRepository.findByIdUserAndIsActive(user2.getSuperior().getIdUser(), true))
                .thenReturn(Optional.empty());

        userService.updateUser(user2);
    }

    @Test (expected = ConflictException.class)
    public void updateUserConflictExceptionTest() {
        Mockito.when(userRepository.findByIdUserAndIsActive(user2.getIdUser(), true))
                .thenReturn(Optional.of(user2));
        Mockito.when(userRepository.findByIdUserAndIsActive(user2.getSuperior().getIdUser(), true))
                .thenReturn(Optional.of(user1));
        Mockito.when(userRepository.findByUsernameAndIsActive(user2.getUsername(), true))
                .thenReturn(Optional.of(user2));

        userService.updateUser(user2);
    }

    @Test
    public void updateUserSuccessTest() {
        Mockito.when(userRepository.findByIdUserAndIsActive(user2.getIdUser(), true))
                .thenReturn(Optional.of(user2));
        Mockito.when(userRepository.findByIdUserAndIsActive(user2.getSuperior().getIdUser(), true))
                .thenReturn(Optional.of(user1));
        Mockito.when(userRepository.save(any())).thenReturn(user2);
        Mockito.when(userRepository.findByUsernameAndIsActive(user2.getUsername(), true))
                .thenReturn(Optional.empty());

        User result = userService.updateUser(user2);
        assertEquals("Bambang Nugroho", result.getName());

    }

    @Test
    public void readAllUsers() {

        PageImpl contents = new PageImpl(new ArrayList());

        Mockito.when(userRepository.findAllByIsActive(true, PageRequest.of(0, Integer.MAX_VALUE)))
                .thenReturn(contents);

        assertEquals(contents, userService.readAllUsers(PageRequest.of(0, Integer.MAX_VALUE)));
    }

    @Test
    public void readUserByIdUser() {
        Mockito.when(userRepository.findByIdUserAndIsActive(user2.getIdUser(), true))
                .thenReturn(Optional.of(user2));

        assertEquals(user2, userService.readUserByIdUser(user2.getIdUser()));
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

        Mockito.when(userRepository.findAllBySuperiorAndIsActive(user1, true, PageRequest.of(0, Integer.MAX_VALUE)))
                .thenReturn(contents);

        assertEquals(contents, userService.readAllUsersByIdSuperior(user2.getSuperior().getIdUser(),
                PageRequest.of(0, Integer.MAX_VALUE)));
    }

    @Test
    public void readAllUsersContaining() {
        assertTrue(true);

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

        assertEquals(ResponseEntity.ok().build(), userService.deleteUser(user1.getIdUser()));
    }
}

