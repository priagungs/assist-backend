package com.future.OfficeInventorySystem.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Set;
import java.util.Set;


@Entity
@Data
@Table(name = "Items")
@TableGenerator(name = "item_id", initialValue = 13216000)
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE , generator ="item_id")
    private Long idItem;

    private String itemName;

    private String picture;

    private Integer price;

    private Integer totalQty;

    private Integer availableQty;

    private String description;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "item")
    private List<Request> request;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "item")
    private List<UserHasItem> owner;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "item")
    private List<ItemTransaction> itemTransaction;


}
