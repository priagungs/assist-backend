package com.future.OfficeInventorySystem.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;




@Entity
@Data
@Table(name ="ItemTransaction")
public class ItemTransaction {

    @Id
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
