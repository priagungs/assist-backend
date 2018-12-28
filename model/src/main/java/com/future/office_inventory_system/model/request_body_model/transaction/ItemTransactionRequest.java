package com.future.office_inventory_system.model.request_body_model.transaction;

import com.future.office_inventory_system.model.request_body_model.item.ItemModelRequest;
import lombok.Data;

@Data
public class ItemTransactionRequest {

    private ItemModelRequest item;
    private Integer boughtQty;
    private Long price;

}
