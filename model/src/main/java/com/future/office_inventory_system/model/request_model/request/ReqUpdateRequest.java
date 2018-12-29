package com.future.office_inventory_system.model.request_model.request;

import com.future.office_inventory_system.model.RequestStatus;
import lombok.Data;

@Data
public class ReqUpdateRequest {

    private Long idRequest;

    private Long idSuperior;

    private Long idAdmin;

    private RequestStatus requestStatus;
}
