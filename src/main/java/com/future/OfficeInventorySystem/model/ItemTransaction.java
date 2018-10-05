package com.future.OfficeInventorySystem.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;




@Entity
@Data
@Table(name ="ItemTransaction")
public class ItemTransaction {

    @Id
    private Long idItemTransaction;

    private Transaction transaction;

    private Item item;

    private Integer boughtQty;
}
