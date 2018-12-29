package com.future.assist.model.request_model.transaction;

import com.future.assist.model.request_model.item.ItemModelRequest;
import lombok.Data;

@Data
public class ItemTransactionRequest {

    private ItemModelRequest item;
    private Integer boughtQty;
    private Long price;

}
