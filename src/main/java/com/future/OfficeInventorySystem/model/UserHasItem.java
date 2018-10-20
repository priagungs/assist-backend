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
@TableGenerator(name = "user_has_item_generator", initialValue = 00000000)
@Table(name="UserHasItem")
public class UserHasItem {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "user_has_item_generator")
    private Long idUserHasItem;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idUser", nullable = false)
    @JsonIgnoreProperties("hasItem")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idItem", nullable = false)
    @JsonIgnoreProperties("owner")
    private Item item;

    private Integer hasQty;

    private Long idAdmin; //the one who handover this item

}
