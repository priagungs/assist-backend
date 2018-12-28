package com.future.office_inventory_system.model.request_body_model;

import com.future.office_inventory_system.model.RequestStatus;
import lombok.Data;

@Data
public class RequestUpdate {

    private Long idRequest;

    private Long idSuperior;

    private Long idAdmin;

    private RequestStatus requestStatus;
}
