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
    private Long transactionId;

    @Setter @Getter
    private Date date;

    @Setter @Getter
    private String supplier;

    @Setter @Getter
    private Long idAdministrator;

}
