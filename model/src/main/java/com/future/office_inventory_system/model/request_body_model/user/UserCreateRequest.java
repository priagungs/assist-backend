package com.future.office_inventory_system.model.request_body_model.user;

import lombok.Data;

@Data
public class UserCreateRequest {

    private Boolean isAdmin;
    private String name;
    private String username;
    private String password;
    private String pictureURL;
    private String division;
    private String role;
    private UserModelRequest superior;

}
