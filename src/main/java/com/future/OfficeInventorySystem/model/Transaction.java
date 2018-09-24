package com.future.OfficeInventorySystem.model;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name="Transaction")
@TableGenerator(name = "transaction_id" , initialValue = 18216000)
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "transaction_id")
    @Setter @Getter
    private Long transactionID;

    @Setter @Getter
    private Date date;

    @Setter @Getter
    private String supplier;

    @Id
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "NIP", nullable = false)
    @Setter @Getter
    private Administrator administrator;
}
