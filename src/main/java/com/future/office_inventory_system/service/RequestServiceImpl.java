package com.future.office_inventory_system.service;

import com.future.office_inventory_system.exception.InvalidValueException;
import com.future.office_inventory_system.exception.NotFoundException;
import com.future.office_inventory_system.model.*;
import com.future.office_inventory_system.repository.RequestRepository;
import com.future.office_inventory_system.value_object.RequestBodyRequestCreate;
import com.future.office_inventory_system.value_object.RequestUpdate;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Data
public class RequestServiceImpl implements RequestService {

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserHasItemService userHasItemService;

    public Request createRequest(RequestBodyRequestCreate requestBody){
        User user= userService.readUserByIdUser(requestBody.getIdUser());

        Item it = itemService.readItemByIdItem(requestBody.getItem().getIdItem());

        Request request = new Request();

        request.setRequestBy(user);
        request.setItem(it);
        request.setRequestDate(requestBody.getRequestDate());

        if(it.getAvailableQty() >= requestBody.getRequestQty()){
            it.setAvailableQty(it.getAvailableQty()-requestBody.getRequestQty());
        } else {
            throw new InvalidValueException(requestBody.getItem().getItemName()+" out of stock");
        }

        request.setReqQty(requestBody.getRequestQty());

        request.setRequestStatus(RequestStatus.REQUESTED);

        requestRepository.save(request);


        return request;
    }

    public Request updateRequest(RequestUpdate requestUpdate){


        Request request = requestRepository.findRequestByIdRequest(requestUpdate.getIdRequest())
                .orElseThrow (()-> new NotFoundException("request not found"));

        if(requestUpdate.getRequestStatus() == RequestStatus.APPROVED){
            request.setApprovedBy(requestUpdate.getIdSuperior());
            request.setApprovedDate(new Date());

        } else if (requestUpdate.getRequestStatus() == RequestStatus.REJECTED) {
            request.setRejectedBy(requestUpdate.getIdSuperior());
            request.setRequestDate(new Date());
            Item item = itemService.readItemByIdItem(request.getIdRequest());
            item.setAvailableQty(request.getReqQty()+item.getAvailableQty());
            itemService.updateItem(item);

        } else if (requestUpdate.getRequestStatus() == RequestStatus.SENT ) {
            request.setHandedOverBy(requestUpdate.getIdSuperior());
            request.setHandedOverDate(new Date());


            UserHasItem userHasItem = new UserHasItem();
            userHasItem.setUser(request.getRequestBy());
            userHasItem.setItem(request.getItem());
            userHasItem.setHasQty(request.getReqQty());

            userHasItemService.createUserHasItem(userHasItem);

        }

        request.setRequestStatus(requestUpdate.getRequestStatus());

        requestRepository.save(request);

        return request;
    }


    public Page<Request> readAllRequest(Pageable pageable){
        return requestRepository.findAll(pageable);
    }

    public Page<Request> readRequestByUser(Pageable pageable, User user){

        return requestRepository.findAllRequestsByRequestBy(user, pageable);
    }

    public Page<Request> readAllRequestBySuperior(Pageable pageable, User superior){
        List<User> users = userService.readAllUsersByIdSuperior(
                superior.getIdUser(),
                PageRequest.of(0, Integer.MAX_VALUE))
                .getContent();
        List<Request> requests = new ArrayList<>();

        for (User user: users) {
            requests.addAll(requestRepository.findAllRequestsByRequestBy(user,pageable)
                    .getContent());
        }
        return new PageImpl<>(requests, pageable, requests.size());
    }

    public Page<Request> readAllRequestByRequestStatus(Pageable pageable, RequestStatus requestStatus){
        return requestRepository.findAllRequestsByRequestStatus(requestStatus, pageable);
    }

    public Page<Request> readAllRequestBySuperiorAndRequestStatus(
            Pageable pageable, User superior, RequestStatus requestStatus){
        List<User> users = userService.readAllUsersByIdSuperior(
                superior.getIdUser(),
                PageRequest.of(0, Integer.MAX_VALUE))
                .getContent();
        List<Request> requests = new ArrayList<>();

        for (User user: users) {
            requests.addAll(requestRepository.findAllRequestsByRequestBy(user,pageable)
                    .getContent());
        }

        List<Request> reqs = new ArrayList<>();

        for (Request request : requests) {
            if(request.getRequestStatus() == requestStatus){
                reqs.add(request);
            }
        }

        return new PageImpl<>(reqs, pageable, reqs.size());
    }

    public ResponseEntity deleteRequest(Request request){
        if(request.getRequestStatus() == RequestStatus.REQUESTED) {
            Item item = itemService.readItemByIdItem(request.getIdRequest());
            item.setAvailableQty(request.getReqQty()+item.getAvailableQty());
            itemService.updateItem(item);
        }

        requestRepository.delete(request);

        return ResponseEntity.ok().build();
    }
}
