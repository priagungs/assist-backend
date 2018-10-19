package com.future.OfficeInventorySystem.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.List;
import java.util.Set;
import java.util.Set;


@Entity
@Data
@Table(name = "Items")
@TableGenerator(name = "item_id", initialValue = 13216000)
@Where(clause = "is_active=1")
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
    @JsonIgnoreProperties("item")
    private List<Request> request;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "item")
    @JsonIgnoreProperties("item")
    private List<UserHasItem> owner;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "item")
    @JsonIgnoreProperties("item")
    private List<ItemTransaction> itemTransaction;

    @Column(name = "is_active")
    private Boolean active = true;


}
