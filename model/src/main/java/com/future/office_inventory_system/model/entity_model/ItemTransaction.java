package com.future.office_inventory_system.model.entity_model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.future.office_inventory_system.model.TableName;
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
    @JoinColumn(name = "idTransaction", nullable = false)
    @JsonIgnore
    private Transaction transaction;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idItem", nullable = false)
    @JsonIgnoreProperties({"itemTransaction", "requests", "owners", "hibernateLazyInitializer", "handler"})
    private Item item;

    private Integer boughtQty;
    private Long price;

}
