package com.future.OfficeInventorySystem.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Entity
@Table(name = "Items")
@TableGenerator(name = "item_id", initialValue = 13216000)
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE , generator ="item_id")
    @Getter @Setter
    private String itemID;

    @Getter @Setter
    private String name;

    @Getter @Setter
    private String picture;

    @Getter @Setter
    private Integer price;

    @Getter @Setter
    private Integer totalQty;

    @Getter @Setter
    private Integer avaibleQty;

    @Getter @Setter
    private String description;

}
