package com.future.office_inventory_system.service;

import com.future.office_inventory_system.model.*;
import com.future.office_inventory_system.value_object.RequestBodyRequestCreate;
import com.future.office_inventory_system.value_object.RequestBodyRequestUpdate;
import com.future.office_inventory_system.value_object.RequestUpdate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;


@Service
public interface RequestService {

    Request createRequest(RequestBodyRequestCreate requestBody);

    Request updateRequest(RequestUpdate requestBody);

    Page<Request> readAllRequest(Pageable pageable);

    Page<Request> readRequestByUser(Pageable pageable, User user);

    Page<Request> readAllRequestBySuperior(Pageable pageable, User superior);

    Page<Request> readAllRequestByRequestStatus(Pageable pageable, RequestStatus requestStatus);

    Page<Request> readAllRequestBySuperiorAndRequestStatus(
            Pageable pageable, User superior, RequestStatus requestStatus);

    ResponseEntity deleteRequest(Request request);
}
