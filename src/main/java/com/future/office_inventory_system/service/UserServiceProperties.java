package com.future.office_inventory_system.service;

import com.future.office_inventory_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

@ConfigurationProperties("service")
public class UserServiceProperties {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserHasItemService userHasItemService;

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public UserHasItemService getUserHasItemService() {
        return userHasItemService;
    }
}
