package com.future.assist;

import com.future.assist.exception.ForbiddenException;
import com.future.assist.exception.UnauthorizedException;
import com.future.assist.mapper.UserMapper;
import com.future.assist.model.entity_model.User;
import com.future.assist.model.request_model.user.UserCreateRequest;
import com.future.assist.model.request_model.user.UserModelRequest;
import com.future.assist.model.request_model.user.UserUpdateRequest;
import com.future.assist.model.response_model.PageResponse;
import com.future.assist.model.response_model.UserResponse;
import com.future.assist.service.service_impl.LoggedinUserInfo;
import com.future.assist.service.service_interface.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
    private UserService userService;

    @Autowired
    private LoggedinUserInfo loggedinUserInfo;

    @Autowired
    private UserMapper userMapper;

    @PostMapping("/users")
    public List<UserResponse> createUser(@RequestBody List<UserCreateRequest> userRequests) {
        if (!loggedinUserInfo.getUser().getIsAdmin()) {
            throw new UnauthorizedException("you are not an admin");
        }

        List<User> users = new ArrayList<>();
        for (UserCreateRequest userRequest : userRequests) {
            users.add(userMapper.createRequestToEntity(userRequest));
        }

        List<UserResponse> result = new ArrayList<>();
        for (User user : users) {
            User createdUser = userService.createUser(user);
            result.add(userMapper.entityToResponse(createdUser));
        }
        return result;
    }

    @GetMapping("/users")
    public PageResponse<UserResponse> readAllUsers(@RequestParam("page") Integer page, @RequestParam("limit") Integer limit,
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
    public UserResponse readUserByIdUser(@PathVariable("idUser") Long idUser) {

        return userMapper.entityToResponse(userService.readUserByIdUser(idUser));

    }

    @GetMapping("/user")
    public UserResponse readUserByUsername(@RequestParam("username") String username) {
        return userMapper.entityToResponse(userService.readUserByUsername(username));
    }

    @PutMapping("/user")
    public UserResponse updateUser(@RequestBody UserUpdateRequest userRequest, HttpSession session) {
        if (!loggedinUserInfo.getUser().getIsAdmin() && loggedinUserInfo.getUser().getIdUser() != userRequest.getIdUser()) {
            throw new UnauthorizedException("You are not permitted to update this user");
        }
        if (loggedinUserInfo.getUser().getIdUser() == userRequest.getIdUser()) {
            session.invalidate();
        }
        return userMapper.entityToResponse(
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
