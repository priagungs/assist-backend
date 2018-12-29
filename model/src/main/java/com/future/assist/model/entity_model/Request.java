package com.future.assist.model.entity_model;

import com.future.assist.model.RequestStatus;
import com.future.assist.model.TableName;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = TableName.REQUEST)
public class Request {
    @Id
    @GeneratedValue
    private Long idRequest;

    @ManyToOne(optional = false)
    @JoinColumn(name = "idUser", nullable = false)
    private User requestBy;

    @ManyToOne(optional = false)
    @JoinColumn(name = "idItem", nullable = false)
    private Item item;

    private Date requestDate;

    private Integer reqQty;

    @Enumerated(EnumType.STRING)
    private RequestStatus requestStatus;

    private Long approvedBy;

    private Date approvedDate;

    private Long rejectedBy;

    private Date rejectedDate;

    private Long handedOverBy;

    private Date handedOverDate;

    private Long returnedBy;

    private Date returnedDate;

}
