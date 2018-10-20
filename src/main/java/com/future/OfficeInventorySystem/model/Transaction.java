package com.future.OfficeInventorySystem.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.OneToMany;
import javax.persistence.ManyToOne;
import javax.persistence.FetchType;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.JoinColumn;

import lombok.Data;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name="Transaction")
@TableGenerator(name = "transaction_generator" , initialValue = 18216000)
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "transaction_generator")
    private Long idTransaction;

    private Date transactionDate;

    private String supplier;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idUser", nullable = false)
    @JsonIgnoreProperties("transaction")
    private User admin;

    @OneToMany(mappedBy = "transaction")
    @JsonIgnoreProperties("transaction")
    private List<ItemTransaction> itemTransactions;

}
