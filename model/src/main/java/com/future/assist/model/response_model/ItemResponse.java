package com.future.assist.model.response_model;

import lombok.Data;

@Data
public class ItemResponse {

    private Long idItem;
    private String itemName;
    private String pictureURL;
    private Long price;
    private Integer totalQty;
    private Integer availableQty;
    private String description;
    private Boolean isActive;

}
