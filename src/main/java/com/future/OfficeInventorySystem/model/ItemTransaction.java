package com.future.OfficeInventorySystem.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.ManyToOne;
import javax.persistence.FetchType;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.JoinColumn;

import lombok.Data;

@Entity
@Data
@TableGenerator(name = "item_transaction_generator")
@Table(name = "itemTransaction")
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
    private Item item;
    private Integer boughtQty;
    private Long price;

}
