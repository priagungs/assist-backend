package com.future.OfficeInventorySystem.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;




@Entity
@Table(name ="ItemTransaction")
public class ItemTransaction {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name ="transactionID", nullable = false)
    @Setter @Getter
    private Transaction transaction;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name ="itemID", nullable = false)
    @Setter @Getter
    private Item item;

    @Id
    @Setter @Getter
    private Integer boughtQty;
}
