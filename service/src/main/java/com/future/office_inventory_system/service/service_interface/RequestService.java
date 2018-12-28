package com.future.office_inventory_system.service.service_interface;

import com.future.office_inventory_system.model.RequestStatus;
import com.future.office_inventory_system.model.entity_model.Item;
import com.future.office_inventory_system.model.entity_model.Request;
import com.future.office_inventory_system.model.entity_model.User;
import com.future.office_inventory_system.model.request_body_model.request.ReqCreateRequest;
import com.future.office_inventory_system.model.request_body_model.request.ReqUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface RequestService {

    List<Request> createRequest(ReqCreateRequest requestBody);

    Request updateRequest(ReqUpdateRequest requestBody);

    Request updateRequestByRequestObject(Request request);

    Page<Request> readAllRequest(Pageable pageable);

    List<Request> readAllRequestsByItem(Item item);

    Page<Request> readRequestByUser(Pageable pageable, User user);

    Page<Request> readAllRequestBySuperior(Pageable pageable, User superior);

    Page<Request> readAllRequestByRequestStatus(Pageable pageable, RequestStatus requestStatus);

    Page<Request> readAllRequestBySuperiorAndRequestStatus(
            Pageable pageable, User superior, RequestStatus requestStatus);

    Page<Request> readAllRequestByUserAndStatus(Pageable pageable, User user, RequestStatus requestStatus);

    ResponseEntity deleteRequest(Request request);

    public Request readRequestByIdRequest(Long id);

    public Request updateRequestStatusToReturned(Request request);
}
