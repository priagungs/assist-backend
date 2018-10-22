package com.future.office_inventory_system.service;

import com.future.office_inventory_system.exception.NotFoundException;
import com.future.office_inventory_system.model.*;
import com.future.office_inventory_system.repository.RequestRepository;
import com.future.office_inventory_system.repository.UserRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Null;
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

    public Page<Request> createRequest(Pageable pageable,RequestBodyRequest requestBody){

        User user= userService.readUserByIdUser(requestBody.getIdUser());

        Item it = new Item();
        List<Request> listNewRequest = new ArrayList<>();
        for(Item item : requestBody.getItems()){
             it = itemService.readItemByIdItem(item.getIdItem());
             Request request = new Request();
             request.setRequestBy(user);
             request.setItem(item);
             request.setRequestDate(new Date());
             request.setReqQty(item.getTotalQty());
             request.setRequestStatus(RequestStatus.REQUESTED);

             listNewRequest.add(request);
//             requestRepository.save(request);
        }

        return new PageImpl<>(listNewRequest, pageable,listNewRequest.size());
    }

    public Request updateRequest(Request request){

        if(userService.readUserByIdUser(request.getRequestBy().getIdUser()) == null) {
            throw new NotFoundException("not found user has request");
        }

        if(request.getApprovedBy() != null){
            if(userService.readUserByIdUser(request.getApprovedBy()) != null){
                throw new NotFoundException("not found user has approved");
            } else {
                if(request.getRejectedBy() != null){
                    if( userService.readUserByIdUser(request.getRejectedBy()) == null){
                        throw new NotFoundException("not found user has reject");
                    }
                }
            }
        }

        if(request.getHandedOverBy() != null){
            if(userService.readUserByIdUser(request.getHandedOverBy()) == null){
                throw new NotFoundException("not found admin has hand over");
            }
        }

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

        requestRepository.delete(request);

        return ResponseEntity.ok().build();
    }
}
