package com.future.assist.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.future.assist.Assist;
import com.future.assist.controller.UserController;
import com.future.assist.configuration.WebSecurityTestConfiguration;
import com.future.assist.mapper.UserMapper;
import com.future.assist.model.entity_model.User;
import com.future.assist.model.request_model.user.UserCreateRequest;
import com.future.assist.model.request_model.user.UserModelRequest;
import com.future.assist.model.request_model.user.UserUpdateRequest;
import com.future.assist.model.response_model.PageResponse;
import com.future.assist.model.response_model.SuperiorResponse;
import com.future.assist.model.response_model.UserResponse;
import com.future.assist.service.service_impl.LoggedinUserInfo;
import com.future.assist.service.service_interface.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
@Import(WebSecurityTestConfiguration.class)
@ContextConfiguration(classes = Assist.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mvc;
    
    @Autowired
    private ObjectMapper mapper;
    
    @MockBean
    private UserService userService;
    
    @MockBean
    private UserMapper userMapper;
    
    @MockBean
    private LoggedinUserInfo loggedinUserInfo;
    
    private UserResponse response;
    private SuperiorResponse superiorResponse;
    private UserCreateRequest request;
    private UserModelRequest superiorModel;
    private UserUpdateRequest updateRequest;
    private PageResponse<UserResponse> pageResponse;
    private UserModelRequest deleteRequest;
    private User user;
    private User superior;
    private User admin;
    private User nonadmin;
    private Page<User> users;

    @Before
    public void setUp() {
        superiorResponse = new SuperiorResponse();
        superiorResponse.setIdUser(0L);
        superiorResponse.setIsAdmin(true);
        superiorResponse.setIsActive(true);
        superiorResponse.setUsername("superior");
        superiorResponse.setName("Superior Name");
        superiorResponse.setRole("Programmer");
        superiorResponse.setDivision("Technology");
        superiorResponse.setPictureURL("photo.png");
        
        response = new UserResponse();
        response.setIdUser(1L);
        response.setIsAdmin(true);
        response.setUsername("dummy");
        response.setName("Dummy User");
        response.setRole("Programmer");
        response.setDivision("Technology");
        response.setPictureURL("photo.png");
        response.setIsActive(true);
        response.setSuperior(superiorResponse);
        
        superiorModel = new UserModelRequest();
        superiorModel.setIdUser(0L);
        
        request = new UserCreateRequest();
        request.setIsAdmin(true);
        request.setName("Dummy User");
        request.setUsername("dummy");
        request.setPassword("pass");
        request.setPictureURL("photo.png");
        request.setDivision("Technology");
        request.setRole("Programmer");
        request.setSuperior(superiorModel);
        
        updateRequest = new UserUpdateRequest();
        updateRequest.setIdUser(1L);
        updateRequest.setIsAdmin(true);
        updateRequest.setName("Dummy User");
        updateRequest.setUsername("dummy");
        updateRequest.setPassword("pass");
        updateRequest.setPictureURL("photo.png");
        updateRequest.setDivision("Technology");
        updateRequest.setRole("Programmer");
        updateRequest.setSuperior(superiorModel);
        
        pageResponse = new PageResponse<>();
        pageResponse.setContent(Arrays.asList(response));
        pageResponse.setTotalPages(1);
        pageResponse.setTotalElements(1L);
        pageResponse.setLast(true);
        pageResponse.setNumberOfElements(1);
        pageResponse.setFirst(true);
        pageResponse.setSize(1);
        pageResponse.setNumber(1);
        
        deleteRequest = new UserModelRequest();
        deleteRequest.setIdUser(1L);
        
        superior = new User();
        superior.setIdUser(0L);
        superior.setIsActive(true);
        
        user = new User();
        user.setIdUser(1L);
        user.setIsActive(true);
        user.setIsAdmin(true);
        user.setUsername("dummy");
        user.setName("Dummy User");
        user.setPassword("pass");
        user.setPictureURL("photo.png");
        user.setRole("Programmer");
        user.setDivision("Technology");
        user.setSuperior(superior);
        
        admin = new User();
        admin.setIsAdmin(true);
    
        nonadmin = new User();
        nonadmin.setIsAdmin(false);
    
        users = new PageImpl<>(Arrays.asList(user));
    }
    
    @Test
    public void createUserUnauthorizedTest() throws Exception {
        when(loggedinUserInfo.getUser()).thenReturn(nonadmin);
        mvc
            .perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(Arrays.asList(request)))
                .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isUnauthorized());
    }
    
    @Test
    public void createUserSuccessTest() throws Exception {
        when(loggedinUserInfo.getUser()).thenReturn(admin);
        when(userMapper.createRequestToEntity(request)).thenReturn(user);
        when(userService.createUser(user)).thenReturn(user);
        when(userMapper.entityToResponse(user)).thenReturn(response);
        
        mvc
            .perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(Arrays.asList(request)))
                .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].idUser", is(response.getIdUser().intValue())));
    }
    
    @Test
    public void readAllUserWithSuperiorSuccessTest() throws Exception {
        when(userService.readAllUsersByIdSuperior(any(), any()))
            .thenReturn(users);
        when(userMapper.pageToPageResponse(users)).thenReturn(pageResponse);
    }
    
    @Test
    public void readAllUsersWithKeywordSuccessTest() throws Exception {
        when(userService.readAllUsersContaining(any(), any()))
            .thenReturn(users);
        when(userMapper.pageToPageResponse(users)).thenReturn(pageResponse);
    
        mvc
            .perform(get("/api/users")
                .param("page", "0")
                .param("limit", "1")
                .param("sort", "idUser")
                .param("keyword", "u"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].idUser", is(response.getIdUser().intValue())))
            .andExpect(jsonPath("$.totalPages", is(pageResponse.getTotalPages())));
    }
    
    @Test
    public void readAllUsersSuccessTest() throws Exception {
        when(userService.readAllUsers(any()))
            .thenReturn(users);
        when(userMapper.pageToPageResponse(users)).thenReturn(pageResponse);
    
        mvc
            .perform(get("/api/users")
                .param("page", "0")
                .param("limit", "1")
                .param("sort", "idUser"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].idUser", is(response.getIdUser().intValue())))
            .andExpect(jsonPath("$.totalPages", is(pageResponse.getTotalPages())));
    }
    
    @Test
    public void readUserByIdUser() throws Exception {
        when(userService.readUserByIdUser(user.getIdUser())).thenReturn(user);
        when(userMapper.entityToResponse(user)).thenReturn(response);
        mvc
            .perform(get("/api/user/" + user.getIdUser()))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.idUser", is(response.getIdUser().intValue())))
            .andExpect(jsonPath("$.username", is(response.getUsername())));
    }
    
    @Test
    public void readUserByUsername() throws Exception {
        when(userService.readUserByUsername(user.getUsername()))
            .thenReturn(user);
        when(userMapper.entityToResponse(user)).thenReturn(response);
        mvc
            .perform(get("/api/user")
                .param("username", user.getUsername()))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.idUser", is(response.getIdUser().intValue())))
            .andExpect(jsonPath("$.username", is(response.getUsername())));
    }
    
    @Test
    public void updateUserUnauthorizedTest() throws Exception {
        when(loggedinUserInfo.getUser()).thenReturn(nonadmin);
        mvc
            .perform(put("/api/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updateRequest))
                .accept(MediaType.APPLICATION_JSON)).andDo(print())
            .andExpect(status().isUnauthorized());
    }
    
    @Test
    public void updateUserSuccessTest() throws Exception {
        when(loggedinUserInfo.getUser()).thenReturn(admin);
        mvc
            .perform(put("/api/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updateRequest))
                .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk());
    }
    
    @Test
    public void deleteUserUnathorizedTest() throws Exception {
        when(loggedinUserInfo.getUser()).thenReturn(nonadmin);
        mvc
            .perform(delete("/api/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(deleteRequest))
                .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isUnauthorized());
    }
    
    @Test
    public void deleteUserForbiddenTest() throws Exception {
        when(loggedinUserInfo.getUser()).thenReturn(user);
        mvc
            .perform(delete("/api/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(deleteRequest))
                .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isForbidden());
    }
    
    @Test
    public void deleteUserSuccessTest() throws Exception {
        when(loggedinUserInfo.getUser()).thenReturn(admin);
        mvc
            .perform(delete("/api/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(deleteRequest))
                .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk());
    }
}