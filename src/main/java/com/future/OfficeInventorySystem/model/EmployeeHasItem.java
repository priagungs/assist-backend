package com.future.OfficeInventorySystem.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@IdClass(EmployeeHasItemID.class)
public class EmployeeHasItem {

    @Id
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "NIP", nullable = false)
    @Setter @Getter
    private Employee employee;

    @Id
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "itemID", nullable = false)
    @Setter @Getter
    private Item item;

    @Setter @Getter
    private Integer hasQty;

    public EmployeeHasItem(Employee employee,
                           Item item, int hasQty) {
        this.employee = employee;
        this.item = item;
        this.hasQty = hasQty;
    }
}
