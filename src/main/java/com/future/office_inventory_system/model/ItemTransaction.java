package com.future.office_inventory_system.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@TableGenerator(name = "item_transaction_generator")
@Table(name = TableName.ITEMTRANSACTION)
public class ItemTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE,generator = "item_transaction_generator")
    private Long idItemTransaction;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idTransaction", nullable = false)
    @JsonIgnoreProperties("itemTransaction")
    private Transaction transaction;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idItem", nullable = false)
    @JsonIgnoreProperties("itemTransaction")
<<<<<<< HEAD
    private Item Item;
=======
    private Item item;
>>>>>>> 9cb565f0447cf870bc120c6e74b9234e90b89adb
    private Integer boughtQty;
    private Long price;

}
