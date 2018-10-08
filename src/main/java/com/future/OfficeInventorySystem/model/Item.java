package com.future.OfficeInventorySystem.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;


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

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "idRequest", nullable = false)
    private List<Request> request;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "idUserHasItem", nullable = false)
    private List<UserHasItem> owner;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "idItemTransaction", nullable = false)
    private List<ItemTransaction> itemTransaction;

}
