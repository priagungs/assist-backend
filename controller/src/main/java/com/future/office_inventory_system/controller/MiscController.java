package com.future.office_inventory_system.controller;

import com.future.office_inventory_system.model.User;
import com.future.office_inventory_system.value_object.LoggedinUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
public class MiscController {

    @Autowired
    LoggedinUserInfo loggedinUserInfo;

    @CrossOrigin(origins = "http://localhost:8080")
    @GetMapping("/login-detail")
    public User getLoginDetail() {
        return loggedinUserInfo.getUser();
    }
}
