package com.future.assist.model.entity_model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.future.assist.model.TableName;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = TableName.TRANSACTION)
public class Transaction {

    @Id
    @GeneratedValue
    private Long idTransaction;

    private Date transactionDate;

    private String supplier;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idUser", nullable = false)
    @JsonIgnoreProperties({"transaction", "superior", "hibernateLazyInitializer", "handler"})
    private User admin;

    @OneToMany(mappedBy = "transaction")
    @JsonIgnoreProperties({"transaction"})
    private List<ItemTransaction> itemTransactions;

}
