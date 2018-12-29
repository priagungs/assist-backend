package com.future.office_inventory_system.model.request_model.transaction;

import com.future.office_inventory_system.model.request_model.user.UserModelRequest;
import lombok.Data;

import java.util.List;

@Data
public class TransactionCreateRequest {

    private String supplier;
    private UserModelRequest admin;
    private List<ItemTransactionRequest> itemTransactions;

}
