package com.future.OfficeInventorySystem.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.TableGenerator;
import org.hibernate.annotations.Where;
import javax.persistence.Table;


import lombok.Data;
import java.util.List;

@Entity
@Data
@Table(name = "item")
@TableGenerator(name = "item_generator", initialValue = 13216000)
@Where(clause = "is_active=1")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE , generator ="item_generator")
    private Long idItem;

    private String itemName;

    private String pictureURL;

    private Long price;

    private Integer totalQty;

    private Integer availableQty;

    private String description;

    @OneToMany(mappedBy = "item")
    @JsonIgnoreProperties("item")
    private List<Request> requests;

    @OneToMany(mappedBy = "item")
    @JsonIgnoreProperties("item")
    private List<UserHasItem> owners;

    @OneToMany(mappedBy = "item")
    @JsonIgnoreProperties("item")
    private List<ItemTransaction> itemTransactions;

    @Column(name = "is_active")
    private Boolean active = true;


}
