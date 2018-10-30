package com.future.office_inventory_system.value_object;

import com.future.office_inventory_system.model.User;
import com.future.office_inventory_system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class LoggedinUserInfo {

    @Autowired
    UserService userService;

    User user;

    public User getUser() {
        if (user == null) {
            user = userService.readUserByUsername(
                    SecurityContextHolder.getContext().getAuthentication().getName());
        }
        return user;
    }
}
