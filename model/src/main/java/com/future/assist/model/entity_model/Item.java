package com.future.assist.model.entity_model;

import com.future.assist.model.TableName;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name = TableName.ITEM)
public class Item {

    @Id
    @GeneratedValue
    private Long idItem;

    private String itemName;

    private String pictureURL;

    private Long price;

    private Integer totalQty;

    private Integer availableQty;

    @Column(length = 1000)
    private String description;

    @OneToMany(mappedBy = "item")
    private List<Request> requests;

    @OneToMany(mappedBy = "item")
    private List<UserHasItem> owners;

    @OneToMany(mappedBy = "item")
    private List<ItemTransaction> itemTransactions;

    private Boolean isActive = true;

}
