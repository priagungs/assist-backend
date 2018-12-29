package com.future.assist.model.request_model.request;

import com.future.assist.model.entity_model.Item;
import lombok.Data;

@Data
public class ReqItemCreateRequest {

    private Item item;
    private Integer requestQty;

}
