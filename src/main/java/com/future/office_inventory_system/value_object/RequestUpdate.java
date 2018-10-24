package com.future.office_inventory_system.value_object;

import com.future.office_inventory_system.model.RequestStatus;
import lombok.Data;

@Data
public class RequestUpdate {

    private Long idRequest;

    private Long idSuperior;

    private RequestStatus requestStatus;
}
