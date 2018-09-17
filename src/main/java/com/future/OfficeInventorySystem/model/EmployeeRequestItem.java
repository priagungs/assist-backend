package com.future.OfficeInventorySystem.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@TableGenerator(name = "employee_request_item")
public class EmployeeRequestItem {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "employee_request_item")
    @Setter @Getter
    private Long reqID;

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



}
