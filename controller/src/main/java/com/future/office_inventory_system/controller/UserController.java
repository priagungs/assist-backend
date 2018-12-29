package com.future.office_inventory_system.controller;

import com.future.office_inventory_system.exception.ForbiddenException;
import com.future.office_inventory_system.exception.UnauthorizedException;
import com.future.office_inventory_system.mapper.UserMapper;
import com.future.office_inventory_system.model.entity_model.User;
import com.future.office_inventory_system.model.request_model.user.UserCreateRequest;
import com.future.office_inventory_system.model.request_model.user.UserModelRequest;
import com.future.office_inventory_system.model.request_model.user.UserUpdateRequest;
import com.future.office_inventory_system.model.response_model.PageResponse;
import com.future.office_inventory_system.model.response_model.SuperiorResponseModel;
import com.future.office_inventory_system.model.response_model.UserResponseModel;
import com.future.office_inventory_system.service.service_impl.LoggedinUserInfo;
import com.future.office_inventory_system.service.service_interface.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    LoggedinUserInfo loggedinUserInfo;

    @Autowired
    UserMapper userMapper;

    @PostMapping("/users")
    public List<UserResponseModel> createUser(@RequestBody List<UserCreateRequest> userRequests) {
        if (!loggedinUserInfo.getUser().getIsAdmin()) {
            throw new UnauthorizedException("you are not an admin");
        }

        List<User> users = new ArrayList<>();
        for (UserCreateRequest userRequest : userRequests) {
            users.add(userMapper.createRequestToEntity(userRequest));
        }

        List<UserResponseModel> result = new ArrayList<>();
        for (User user : users) {
            User createdUser = userService.createUser(user);
            result.add(userMapper.entityToResponseModel(createdUser));
        }
        return result;
    }

    @GetMapping("/users")
    public PageResponse<UserResponseModel> readAllUsers(@RequestParam("page") Integer page, @RequestParam("limit") Integer limit,
                                           @RequestParam(value = "idSuperior", required = false) Long idSuperior,
                                           @RequestParam(value = "keyword", required = false) String keyword,
                                           @RequestParam("sort") String sort) {
        if (idSuperior != null) {
            return userMapper.pageToPageResponse(userService.readAllUsersByIdSuperior(idSuperior,
                    PageRequest.of(page, limit, Sort.Direction.ASC, sort)));
        } else if (keyword != null) {
            return userMapper.pageToPageResponse(userService.readAllUsersContaining(keyword,
                    PageRequest.of(page, limit, Sort.Direction.ASC, sort)));
        } else {
            return userMapper.pageToPageResponse(userService.readAllUsers(
                    PageRequest.of(page, limit, Sort.Direction.ASC, sort)));
        }
    }

    @GetMapping("/user/{idUser}")
    public UserResponseModel readUserByIdUser(@PathVariable("idUser") Long idUser) {

        return userMapper.entityToResponseModel(userService.readUserByIdUser(idUser));

    }

    @GetMapping("/user")
    public UserResponseModel readUserByUsername(@RequestParam("username") String username) {
        return userMapper.entityToResponseModel(userService.readUserByUsername(username));
    }

    @PutMapping("/user")
    public UserResponseModel updateUser(@RequestBody UserUpdateRequest userRequest, HttpSession session) {
        if (!loggedinUserInfo.getUser().getIsAdmin() && loggedinUserInfo.getUser().getIdUser() != userRequest.getIdUser()) {
            throw new UnauthorizedException("You are not permitted to update this user");
        }
        if (loggedinUserInfo.getUser().getIdUser() == userRequest.getIdUser()) {
            session.invalidate();
        }
        return userMapper.entityToResponseModel(
                userService.updateUser(userMapper.updateRequestToEntity(userRequest)));
    }

    @DeleteMapping("/user")
    public ResponseEntity deleteUser(@RequestBody UserModelRequest user) {
        if (!loggedinUserInfo.getUser().getIsAdmin()) {
            throw new UnauthorizedException("You are not permitted to delete this user");
        }
        if (loggedinUserInfo.getUser().getIdUser() == user.getIdUser()) {
            throw new ForbiddenException("You are not permitted to delete yourself");
        }
        return userService.deleteUser(user.getIdUser());
    }
}
