package com.future.assist.model.response_model;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class TransactionResponse {

    private Long idTransaction;
    private Date transactionDate;
    private String supplier;
    private UserResponse admin;
    private List<ItemTransactionResponse> itemTransactions;

}
