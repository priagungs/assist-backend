package com.future.office_inventory_system.value_object;

import com.future.office_inventory_system.model.Item;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class RequestBodyRequestCreate {

    private Long IdUser;

    private Item item;

    private Integer requestQty;

    private Date requestDate = new Date();
}
