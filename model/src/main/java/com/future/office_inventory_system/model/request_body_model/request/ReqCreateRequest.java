package com.future.office_inventory_system.model.request_body_model.request;

import lombok.Data;

import java.util.List;

@Data
public class ReqCreateRequest {

    private Long idUser;

    private List<ReqItemCreateRequest> items;

}
