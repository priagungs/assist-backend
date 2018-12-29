package com.future.assist.model.entity_model;

import com.future.assist.model.TableName;
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
    private Transaction transaction;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idItem", nullable = false)
    private Item item;

    private Integer boughtQty;
    private Long price;

}
