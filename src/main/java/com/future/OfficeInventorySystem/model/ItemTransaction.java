package com.future.OfficeInventorySystem.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;

public class ItemTransaction {

    @Id
    @Setter @Getter
    private Long transactionId;

    @Id
    @Setter @Getter
    private Long itemId;

    @Id
    @Setter @Getter
    private Long boughtQty;
}
