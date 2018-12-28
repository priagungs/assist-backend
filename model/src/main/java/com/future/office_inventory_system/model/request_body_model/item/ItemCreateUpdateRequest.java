package com.future.office_inventory_system.model.request_body_model.item;

import lombok.Data;

@Data
public class ItemCreateUpdateRequest {

    private String itemName;
    private String description;
    private String pictureURL;
    private Long price;
    private Integer totalQty;

}
