package com.future.assist.model.entity_model;

import com.future.assist.model.TableName;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = TableName.USERHASITEM)
public class UserHasItem {

    @Id
    @GeneratedValue
    private Long idUserHasItem;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idUser", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idItem", nullable = false)
    private Item item;

    private Integer hasQty;

}
