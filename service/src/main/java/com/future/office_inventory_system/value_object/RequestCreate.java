package com.future.office_inventory_system.value_object;

import lombok.Data;

import java.util.List;

@Data
public class RequestCreate {

    private Long idUser;

    private List<ItemRequest> items;

}
