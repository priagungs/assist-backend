package com.future.office_inventory_system.model.request_body_model;

import com.future.office_inventory_system.model.entity_model.Item;
import lombok.Data;

@Data
public class RequestItem {

    private Item item;
    private Integer requestQty;

}
