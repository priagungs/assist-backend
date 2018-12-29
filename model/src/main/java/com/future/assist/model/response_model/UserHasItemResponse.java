package com.future.assist.model.response_model;

import lombok.Data;

@Data
public class UserHasItemResponse {

    private Long idUserHasItem;
    private UserResponse user;
    private ItemResponse item;
    private Integer hasQty;

}
