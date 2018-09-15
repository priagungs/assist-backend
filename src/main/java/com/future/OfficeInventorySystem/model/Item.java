package com.future.OfficeInventorySystem.model;


public class Item {

    private String itemID;

    private String name;

    private String picture;

    private Integer price;

    private Integer totalQty;

    private Integer avaibleQty;

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
