package com.future.assist.service.service_impl;

import com.future.assist.model.entity_model.User;
import com.future.assist.service.service_interface.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LoggedinUserInfoTest {

    @Autowired
    private LoggedinUserInfo loggedinUserInfo;

    @MockBean
    private UserService userService;

    private User user1;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        user1 = new User();
        user1.setUsername("priagung");
        user1.setName("Priagung Satyagama");
        user1.setIsAdmin(true);
        user1.setIsActive(true);
        user1.setHasItems(new ArrayList<>());
    }

    @Test
    public void getUserSuccess() {
        user1 = Mockito.mock(User.class);
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(user1);

        when(userService.readUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName()))
                .thenReturn(user1);

        assertEquals(user1.getUsername(), loggedinUserInfo.getUser().getUsername());
    }
}