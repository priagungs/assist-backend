package com.future.OfficeInventorySystem.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Entity
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

    public Item (String itemID, String name,
                 String picture, Integer price,
                 Integer totalQty, Integer avaibleQty,
                 String description) {
        this.itemID = itemID;
        this.name = name;
        this.picture = picture;
        this.price = price;
        this.totalQty = totalQty;
        this.avaibleQty = avaibleQty;
        this.description = description;
    }


}
