package com.future.OfficeInventorySystem.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;


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
    private List<ItemTransaction> itemTransaction;

}
