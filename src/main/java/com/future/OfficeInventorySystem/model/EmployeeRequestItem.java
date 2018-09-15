package com.future.OfficeInventorySystem.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
public class EmployeeRequestItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO);
    @Setter @Getter
    private long reqID;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "NIP", nullable = false)
    @Setter @Getter
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "itemID", nullable = false)
    @Setter @Getter
    private Item item;

    @Setter @Getter
    private Integer reqQty;

    @Enumerated(EnumType.STRING)
    @Setter @Getter
    private Status status;


    public EmployeeHasItem(Employee employee,
                           Item item, int reqQty) {
        this.employee = employee;
        this.item = item;
        this.reqQty = reqQty;
        this.status = Status.REQUESTED;
    }


}
