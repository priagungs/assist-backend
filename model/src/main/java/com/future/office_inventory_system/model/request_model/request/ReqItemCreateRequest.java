package com.future.office_inventory_system.model.request_model.request;

import com.future.office_inventory_system.model.entity_model.Item;
import lombok.Data;

@Data
public class ReqItemCreateRequest {

    private Item item;
    private Integer requestQty;

}
