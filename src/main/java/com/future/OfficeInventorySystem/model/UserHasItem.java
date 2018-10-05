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

    private User user;

    private Item item;

    private Integer hasQty;

}
