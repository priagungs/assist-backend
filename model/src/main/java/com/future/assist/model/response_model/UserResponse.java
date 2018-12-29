package com.future.assist.model.response_model;

import lombok.Data;

@Data
public class UserResponse {

    private Long idUser;
    private Boolean isAdmin;
    private String username;
    private String name;
    private String role;
    private String division;
    private String pictureURL;
    private Boolean isActive;
    private SuperiorResponse superior;

}
