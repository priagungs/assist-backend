package com.future.office_inventory_system.controller;

import com.future.office_inventory_system.exception.ForbiddenException;
import com.future.office_inventory_system.exception.UnauthorizedException;
import com.future.office_inventory_system.model.entity_model.User;
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

    @PostMapping("/users")
    public List<User> createUser(@RequestBody List<User> users) {
        if (!loggedinUserInfo.getUser().getIsAdmin()) {
            throw new UnauthorizedException("you are not an admin");
        }

        List<User> result = new ArrayList<>();
        for (User user : users) {
            result.add(userService.createUser(user));
        }
        return result;
    }

    @GetMapping("/users")
    public Page<User> readAllUsers(@RequestParam("page") Integer page, @RequestParam("limit") Integer limit,
                                   @RequestParam(value = "idSuperior", required = false) Long idSuperior,
                                   @RequestParam(value = "keyword", required = false) String keyword,
                                   @RequestParam("sort") String sort) {
        if (idSuperior != null) {
            return userService.readAllUsersByIdSuperior(idSuperior,
                    PageRequest.of(page, limit, Sort.Direction.ASC, sort));
        } else if (keyword != null) {
            return userService.readAllUsersContaining(keyword,
                    PageRequest.of(page, limit, Sort.Direction.ASC, sort));
        } else {
            return userService.readAllUsers(PageRequest.of(page, limit, Sort.Direction.ASC, sort));
        }
    }

    @GetMapping("/user/{idUser}")
    public User readUserByIdUser(@PathVariable("idUser") Long idUser) {
        return userService.readUserByIdUser(idUser);
    }

    @GetMapping("/user")
    public User readUserByUsername(@RequestParam("username") String username) {
        return userService.readUserByUsername(username);
    }

    @PutMapping("/user")
    public User updateUser(@RequestBody User user, HttpSession session) {
        if (!loggedinUserInfo.getUser().getIsAdmin() && loggedinUserInfo.getUser().getIdUser() != user.getIdUser()) {
            throw new UnauthorizedException("You are not permitted to update this user");
        }
        session.invalidate();
        return userService.updateUser(user);
    }

    @DeleteMapping("/user")
    public ResponseEntity deleteUser(@RequestBody User user) {
        if (!loggedinUserInfo.getUser().getIsAdmin()) {
            throw new UnauthorizedException("You are not permitted to delete this user");
        }
        if (loggedinUserInfo.getUser().getIdUser() == user.getIdUser()) {
            throw new ForbiddenException("You are not permitted to delete yourself");
        }
        return userService.deleteUser(user.getIdUser());
    }
}
