package com.future.assist.model.request_model.request;

import com.future.assist.model.RequestStatus;
import lombok.Data;

@Data
public class ReqUpdateRequest {
    private Long idRequest;
    private Long idSuperior;
    private Long idAdmin;
    private RequestStatus requestStatus;
}
