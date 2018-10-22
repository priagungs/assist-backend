package com.future.office_inventory_system.model;

import lombok.Data;

import java.util.List;

@Data
public class RequestBodyRequest {

    private Long IdUser;

    private List<Item> items;
}
