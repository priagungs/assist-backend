package com.future.assist.model.request_model.request;

import lombok.Data;

import java.util.List;

@Data
public class ReqCreateRequest {
    private Long idUser;
    private List<ReqItemCreateRequest> items;
}
