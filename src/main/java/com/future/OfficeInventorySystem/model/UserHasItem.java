package com.future.OfficeInventorySystem.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

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
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idItem", nullable = false)
    private Item item;

    private Integer hasQty;

}
