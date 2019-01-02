package com.future.assist.service.service_impl;

import com.future.assist.model.entity_model.User;
import com.future.assist.service.service_interface.UserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserAuthServiceTest {
    @Autowired
    UserAuthService authService;

    @Autowired
    LoggedinUserInfo loggedinUserInfo;

    @MockBean
    UserService userService;

    private User user;

    @Before
    public void setUp() {
        user = new User();
        user.setIdUser(1L);
        user.setIsAdmin(true);
        user.setIsActive(true);
        user.setName("Dummy User");
        user.setUsername("dummy");
        user.setPassword("password");
    }

    @Test
    public void loadUserByUsernameSuccessTest() {
        Mockito.when(userService
                .readUserByUsername(user.getUsername())).thenReturn(user);
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                AuthorityUtils.NO_AUTHORITIES
        );
        Assert.assertEquals(userDetails, authService.loadUserByUsername(user.getUsername()));
    }
}