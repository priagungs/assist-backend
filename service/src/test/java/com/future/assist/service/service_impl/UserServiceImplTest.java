package com.future.assist.service.service_impl;

import com.future.assist.exception.ConflictException;
import com.future.assist.exception.InvalidValueException;
import com.future.assist.exception.NotFoundException;
import com.future.assist.model.RequestStatus;
import com.future.assist.model.entity_model.Item;
import com.future.assist.model.entity_model.Request;
import com.future.assist.model.entity_model.User;
import com.future.assist.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserServiceImplTest {

    @Autowired
    private UserServiceImpl userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserHasItemServiceImpl userHasItemService;

    @MockBean
    private RequestServiceImpl requestService;

    private User user1;
    private User user2;

    @MockBean
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
        user3.setSuperior(user1);

    }

    @Test(expected = NotFoundException.class)
    public void createUserSuperiorNotFoundTest() {
        Mockito.when(userRepository.findByIdUserAndIsActive(user2.getSuperior().getIdUser(), true))
                .thenReturn(Optional.empty());

        userService.createUser(user2);
    }

    @Test(expected = ConflictException.class)
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

        assertEquals(user2, userService.createUser(user2));
    }

    @Test(expected = NotFoundException.class)
    public void updateUserNotFoundUserTest() {
        Mockito.when(userRepository.findByIdUserAndIsActive(user1.getIdUser(), true))
                .thenReturn(Optional.empty());

        userService.updateUser(user1);
    }

    @Test(expected = InvalidValueException.class)
    public void updateUserCircularSuperiorTest() {


        Mockito.when(userRepository.findByIdUserAndIsActive(user2.getSuperior().getIdUser(), true))
                .thenReturn(Optional.of(user1));

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

    @Test(expected = ConflictException.class)
    public void updateUserConflictExceptionTest() {
        user1.setIdUser(1L);
        user2.setIdUser(2L);
        System.out.println("user1 :" + user1.getIdUser());
        System.out.println("user2 :" + user2.getIdUser());
        Mockito.when(userRepository.findByIdUserAndIsActive(user2.getIdUser(), true))
                .thenReturn(Optional.of(user1));
        Mockito.when(userRepository.findByIdUserAndIsActive(user3.getSuperior().getIdUser(), true)
        ).thenReturn(Optional.of(user1));
        Mockito.when(userRepository.findByUsernameAndIsActive(user2.getUsername(), true))
                .thenReturn(Optional.of(user2));
        userService.updateUser(user2);
    }

    @Test
    public void updateUserSuccessTest() {
        user1.setIdUser(1L);
        user2.setIdUser(2L);
        System.out.println("user1 :" + user1.getIdUser());
        System.out.println("user2 :" + user2.getIdUser());
        Mockito.when(userRepository.findByIdUserAndIsActive(user2.getIdUser(), true))
                .thenReturn(Optional.of(user1));
        Mockito.when(userRepository.findByIdUserAndIsActive(user3.getSuperior().getIdUser(), true)
        ).thenReturn(Optional.of(user1));
        Mockito.when(userRepository.findByUsernameAndIsActive(user2.getUsername(), true))
                .thenReturn(Optional.empty());
        Mockito.when(userRepository.save(any()))
                .thenReturn(user1);
        User result = userService.updateUser(user2);
        assertEquals(user1.getName(), result.getName());

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
        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);
        Mockito.when(userRepository.findByNameIgnoreCaseContainingAndIsActive("mie", true, PageRequest.of(0, Integer.MAX_VALUE)))
                .thenReturn(new PageImpl(users));

        assertEquals(
                users.size(),
                userService.readAllUsersContaining("mie", PageRequest.of(0, Integer.MAX_VALUE)
                ).getContent().size()
        );
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

        Item item1 = new Item();
        item1.setIdItem(Long.valueOf(123456));
        item1.setItemName("Buku");
        item1.setTotalQty(10);
        item1.setAvailableQty(5);

        Request request = new Request();
        request.setRequestBy(user1);
        request.setItem(item1);
        request.setReqQty(2);
        request.setRequestStatus(RequestStatus.REQUESTED);

        List<Request> requests = new ArrayList<>();
        requests.add(request);

        user1.setRequests(requests);

        Mockito.when(requestService.deleteRequest(any()))
                .thenReturn(ResponseEntity.ok().build());
        Mockito.when(userRepository.save(any()))
                .thenReturn(user1);

        assertEquals(ResponseEntity.ok().build(), userService.deleteUser(user1.getIdUser()));
    }
}

