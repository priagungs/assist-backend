package com.future.office_inventory_system.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = TableName.ITEMTRANSACTION)
public class ItemTransaction {

    @Id
    @GeneratedValue
    private Long idItemTransaction;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idTransaction")
    @JsonIgnore
    private Transaction transaction;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idItem", nullable = false)

    @JsonIgnoreProperties({"itemTransaction", "requests", "owners"})
    private Item item;

    private Integer boughtQty;
    private Long price;

}
