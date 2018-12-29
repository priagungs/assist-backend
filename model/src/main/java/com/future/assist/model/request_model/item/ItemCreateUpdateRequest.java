package com.future.assist.model.request_model.item;

import lombok.Data;

@Data
public class ItemCreateUpdateRequest {

    private String itemName;
    private String description;
    private String pictureURL;
    private Long price;
    private Integer totalQty;

}
