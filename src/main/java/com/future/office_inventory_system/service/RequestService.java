package com.future.office_inventory_system.service;

import com.future.office_inventory_system.model.Request;
import com.future.office_inventory_system.model.RequestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;


@Service
public interface RequestService {

    ResponseEntity createRequest(Request request);

    ResponseEntity updateRequest(Request request);

    Page<Request> readAllRequest(Pageable pageable);

    Page<Request> readRequestByIdUser(Pageable pageable, Long idUser);

    Page<Request> readAllRequestByIdSuperior(Pageable pageable, Long id);

    Page<Request> readAllRequestByRequestStatus(Pageable pageable, RequestStatus requestStatus);

    Page<Request> readAllRequestByIdSuperiorAndRequestStatus(
            Pageable pageable, Long id, RequestStatus requestStatus);

    ResponseEntity deleteRequest(Request request);
}
