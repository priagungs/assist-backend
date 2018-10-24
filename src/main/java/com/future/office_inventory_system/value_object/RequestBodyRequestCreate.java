package com.future.office_inventory_system.value_object;

import com.future.office_inventory_system.model.Item;
import lombok.Data;

import java.util.List;

@Data
public class RequestBodyRequestCreate {

    private Long IdUser;

    private List<Item> items;

    private Integer requestQty;
}
