package com.future.OfficeInventorySystem.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Data
public class UserHasItem {

    @Id
    private long idUserHasItem;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "idUser", nullable = false)
    private User user;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "idItem", nullable = false)
    private Item item;

    private Integer hasQty;

}
