package com.future.office_inventory_system.model.request_body_model;

import lombok.Data;

import java.util.List;

@Data
public class RequestCreate {

    private Long idUser;

    private List<RequestItem> items;

}
