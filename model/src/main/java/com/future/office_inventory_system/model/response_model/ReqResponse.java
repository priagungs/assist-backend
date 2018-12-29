package com.future.office_inventory_system.model.response_model;

import com.future.office_inventory_system.model.RequestStatus;
import lombok.Data;

import java.util.Date;

@Data
public class ReqResponse {

    private Long idRequest;
    private UserResponse requestBy;
    private ItemResponse item;
    private Date requestDate;
    private Integer reqQty;
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
