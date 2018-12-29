package com.future.office_inventory_system.controller;

import com.future.office_inventory_system.exception.InvalidValueException;
import com.future.office_inventory_system.exception.UnauthorizedException;
import com.future.office_inventory_system.mapper.ReqMapper;
import com.future.office_inventory_system.model.RequestStatus;
import com.future.office_inventory_system.model.entity_model.Request;
import com.future.office_inventory_system.model.request_model.request.ReqCreateRequest;
import com.future.office_inventory_system.model.request_model.request.ReqModelRequest;
import com.future.office_inventory_system.model.request_model.request.ReqUpdateRequest;
import com.future.office_inventory_system.model.response_model.PageResponse;
import com.future.office_inventory_system.model.response_model.ReqResponse;
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

    @Autowired
    ReqMapper reqMapper;

    @PostMapping("/requests")
    List<ReqResponse> createRequests(@RequestBody ReqCreateRequest requestBody) {
        List<Request> requests = requestService.createRequest(requestBody);
        List<ReqResponse> reqResponses = new ArrayList<>();
        for (Request request : requests) {
            reqResponses.add(reqMapper.entityToResponse(request));
        }
        return reqResponses;
    }

    @GetMapping("/requests")
    PageResponse<ReqResponse> readRequests(@RequestParam("page") Integer page,
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
            return reqMapper.pageToPageResponse(requestService.readAllRequestByUserAndStatus(
                    PageRequest.of(page, limit, direction, sort), userService.readUserByIdUser(idUser), status));
        } else if (idUser != null) {
            return reqMapper.pageToPageResponse(requestService.readRequestByUser(
                    PageRequest.of(page, limit, direction, sort), userService.readUserByIdUser(idUser)));
        } else if (idSuperior != null && status != null) {
            return reqMapper.pageToPageResponse(requestService.readAllRequestBySuperiorAndRequestStatus(
                    PageRequest.of(page, limit, direction, sort), userService.readUserByIdUser(idSuperior), status));
        } else if (idSuperior != null) {
            return reqMapper.pageToPageResponse(requestService.readAllRequestBySuperior(
                    PageRequest.of(page, limit, direction, sort), userService.readUserByIdUser(idSuperior)));
        } else if (status != null) {
            return reqMapper.pageToPageResponse(requestService.readAllRequestByRequestStatus(
                    PageRequest.of(page, limit, direction, sort), status));
        } else {
            return reqMapper.pageToPageResponse(requestService.readAllRequest(
                    PageRequest.of(page, limit, direction, sort)));
        }
    }

    @PutMapping("/requests")
    List<ReqResponse> updateRequest(@RequestBody List<ReqUpdateRequest> requestBodies) {
        List<Request> result = new ArrayList<>();
        for (ReqUpdateRequest requestBody : requestBodies) {
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

        List<ReqResponse> responses = new ArrayList<>();
        for (Request request : result) {
            responses.add(reqMapper.entityToResponse(request));
        }
        return responses;
    }

    @DeleteMapping("/requests")
    ResponseEntity deleteRequest(@RequestBody ReqModelRequest request) {
        Request req = requestService.readRequestByIdRequest(request.getIdRequest());
        if (userInfo.getUser().getIdUser() != req.getRequestBy().getIdUser()) {
            throw new UnauthorizedException("You can't delete this request");
        }
        requestService.deleteRequest(req);
        return ResponseEntity.ok().build();
    }
}
