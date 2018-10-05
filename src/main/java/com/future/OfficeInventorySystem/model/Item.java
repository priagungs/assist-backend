package com.future.OfficeInventorySystem.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Entity
@Data
@Table(name = "Items")
@TableGenerator(name = "item_id", initialValue = 13216000)
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE , generator ="item_id")
    private String idItem;

    private String itemName;

    private String picture;

    private Integer price;

    private Integer totalQty;

    private Integer availableQty;

    private String description;

    private Request request;

    private UserHasItem owner;

    private ItemTransaction itemTransaction;

}
