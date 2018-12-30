package com.future.assist.service.service_impl;

import com.future.assist.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;


import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserServiceImplTest {

    @Autowired
    private UserServiceImpl userService;

    @MockBean
    private UserRepository userRepository;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void createUser() {
        assertTrue(true);
    }

    @Test
    public void updateUser() {
        assertTrue(true);

    }

    @Test
    public void readAllUsers() {
        assertTrue(true);

    }

    @Test
    public void readUserByIdUser() {
        assertTrue(true);

    }

    @Test
    public void readAllUsersByIdSuperior() {
        assertTrue(true);

    }

    @Test
    public void readAllUsersContaining() {
        assertTrue(true);

    }

    @Test
    public void readUserByUsername() {
        assertTrue(true);

    }

    @Test
    public void deleteUser() {
        assertTrue(true);

    }
}

