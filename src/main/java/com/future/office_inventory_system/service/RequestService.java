package com.future.office_inventory_system.service;

import com.future.office_inventory_system.model.*;
import com.future.office_inventory_system.value_object.RequestCreate;
import com.future.office_inventory_system.value_object.RequestUpdate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;

import java.util.List;


@Service
public interface RequestService {

    List<Request> createRequest(RequestCreate requestBody);

    Request updateRequest(RequestUpdate requestBody);

    Request updateRequestByRequestObject(Request request);

    Page<Request> readAllRequest(Pageable pageable);

    List<Request> readAllRequestsByItem(Item item);

    Page<Request> readRequestByUser(Pageable pageable, User user);

    Page<Request> readAllRequestBySuperior(Pageable pageable, User superior);

    Page<Request> readAllRequestByRequestStatus(Pageable pageable, RequestStatus requestStatus);

    Page<Request> readAllRequestBySuperiorAndRequestStatus(
            Pageable pageable, User superior, RequestStatus requestStatus);

    ResponseEntity deleteRequest(Request request);

    public Request readRequestByIdRequest(Long id);

    public Request updateRequestStatusToReturned(Request request);
}
