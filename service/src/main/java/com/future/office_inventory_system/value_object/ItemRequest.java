package com.future.office_inventory_system.value_object;

import com.future.office_inventory_system.model.Item;
import lombok.Data;

@Data
public class ItemRequest {

    private Item item;
    private Integer requestQty;

}
