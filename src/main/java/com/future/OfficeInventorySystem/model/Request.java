package com.future.OfficeInventorySystem.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Data
@Table(name = "Request")
@TableGenerator(name = "request_generator")
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "request_generator")
    private Long idRequest;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idUser", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idItem", nullable = false)
    private Item item;

    private Date requestDate;

    private Integer reqQty;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idSuperior", nullable = false)
    private User superior;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idAdministrator", nullable = false)
    private User administrator;




}
