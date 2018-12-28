package com.future.office_inventory_system.controller;

import com.future.office_inventory_system.exception.InvalidValueException;
import com.future.office_inventory_system.exception.UnauthorizedException;
import com.future.office_inventory_system.model.RequestStatus;
import com.future.office_inventory_system.model.entity_model.Request;
import com.future.office_inventory_system.model.request_body_model.RequestCreate;
import com.future.office_inventory_system.model.request_body_model.RequestUpdate;
import com.future.office_inventory_system.service.service_impl.LoggedinUserInfo;
import com.future.office_inventory_system.service.service_interface.RequestService;
import com.future.office_inventory_system.service.service_interface.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class RequestController {

    @Autowired
    RequestService requestService;

    @Autowired
    UserService userService;

    @Autowired
    LoggedinUserInfo userInfo;

    @PostMapping("/requests")
    List<Request> createRequests(@RequestBody RequestCreate requestBody) {
        return requestService.createRequest(requestBody);
    }

    @GetMapping("/requests")
    Page<Request> readRequests(@RequestParam("page") Integer page,
                               @RequestParam("limit") Integer limit,
                               @RequestParam(required = false, name = "idUser") Long idUser,
                               @RequestParam(required = false, name = "idSuperior") Long idSuperior,
                               @RequestParam(required = false, name = "status") RequestStatus status,
                               @RequestParam("sort") String sort) {
        Sort.Direction direction = Sort.Direction.ASC;
        if (sort.equals("requestDate") || sort.equals("rejectedDate") || sort.equals("handedOverDate") ||
                sort.equals("returnedDate")) {
            direction = Sort.Direction.DESC;
        }
        if (idUser != null && status != null) {
            return requestService.readAllRequestByUserAndStatus(PageRequest.of(page, limit, direction, sort),
                    userService.readUserByIdUser(idUser), status);
        } else if (idUser != null) {
            return requestService.readRequestByUser(PageRequest.of(page, limit, direction, sort), userService.readUserByIdUser(idUser));
        } else if (idSuperior != null && status != null) {
            return requestService.readAllRequestBySuperiorAndRequestStatus(PageRequest.of(page, limit, direction, sort),
                    userService.readUserByIdUser(idSuperior), status);
        } else if (idSuperior != null) {
            return requestService.readAllRequestBySuperior(PageRequest.of(page, limit, direction, sort),
                    userService.readUserByIdUser(idSuperior));
        } else if (status != null) {
            return requestService.readAllRequestByRequestStatus(PageRequest.of(page, limit, direction, sort), status);
        } else {
            return requestService.readAllRequest(PageRequest.of(page, limit, direction, sort));
        }
    }

    @PutMapping("/requests")
    List<Request> updateRequest(@RequestBody List<RequestUpdate> requestBodies) {
        List<Request> result = new ArrayList<>();
        for (RequestUpdate requestBody : requestBodies) {
            if (requestBody.getRequestStatus() == RequestStatus.APPROVED ||
                    requestBody.getRequestStatus() == RequestStatus.REJECTED) {
                if (userInfo.getUser().getIdUser() != requestBody.getIdSuperior()) {
                    throw new UnauthorizedException("You can't approved this request");
                }
                result.add(requestService.updateRequest(requestBody));
            } else if (requestBody.getRequestStatus() == RequestStatus.SENT) {
                if (!userInfo.getUser().getIsAdmin() &&
                        requestBody.getIdAdmin() != userInfo.getUser().getIdUser()) {
                    throw new UnauthorizedException("You can't hand over this request");
                }
                result.add(requestService.updateRequest(requestBody));
            } else {
                throw new InvalidValueException("Invalid request body");
            }
        }
        return result;
    }

    @DeleteMapping("/requests")
    ResponseEntity deleteRequest(@RequestBody Request request) {
        Request req = requestService.readRequestByIdRequest(request.getIdRequest());
        if (userInfo.getUser().getIdUser() != req.getRequestBy().getIdUser()) {
            throw new UnauthorizedException("You can't delete this request");
        }
        requestService.deleteRequest(request);
        return ResponseEntity.ok().build();
    }
}
