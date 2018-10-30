package com.future.office_inventory_system.controller;

import com.future.office_inventory_system.model.User;
import com.future.office_inventory_system.service.UserService;
import com.future.office_inventory_system.value_object.LoggedinUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @Autowired
    LoggedinUserInfo loggedinUserInfo;

    @Autowired
    UserService userService;

    @GetMapping("/login-detail")
    public User getLoginDetail() {
        return loggedinUserInfo.getUser();
    }
}
