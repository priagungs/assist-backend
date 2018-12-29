package com.future.assist.model.entity_model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
    @JsonIgnoreProperties({"hasItem", "superior", "hibernateLazyInitializer", "handler"})
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idItem", nullable = false)
    @JsonIgnoreProperties({"owner", "hibernateLazyInitializer", "handler"})
    private Item item;

    private Integer hasQty;

}
