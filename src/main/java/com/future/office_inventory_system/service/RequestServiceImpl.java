package com.future.office_inventory_system.service;

import com.future.office_inventory_system.exception.UserNotFoundException;
import com.future.office_inventory_system.model.Item;
import com.future.office_inventory_system.model.Request;
import com.future.office_inventory_system.model.RequestStatus;
import com.future.office_inventory_system.model.User;
import com.future.office_inventory_system.repository.RequestRepository;
import org.hibernate.validator.internal.constraintvalidators.bv.number.bound.decimal.DecimalMaxValidatorForBigInteger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RequestServiceImpl implements RequestService {

    @Autowired
    RequestRepository requestRepository;

    @Autowired
    UserService userService;

    @Autowired
    ItemService itemService;

    public ResponseEntity createRequest(Request request){
        try {
            User user= userService.readUserByIdUser(request.getRequestBy());
//            Item item= itemService.readItemById(request.getItem().getIdItem())
        }
        catch (UserNotFoundException e)//need add Item not found exception
        {
//          throw e;
//          if user not found
            throw new UserNotFoundException("user not found");
//            if item not found
//            throw new ItemNotFoundException
        }

        requestRepository.save(request);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity updateRequest(Request request){




        return ResponseEntity.ok().build();
    }

    public Page<Request> readAllRequest(Pageable pageable){
        return requestRepository.findAll(pageable); }



    public Page<Request> readRequestByIdUser(Pageable pageable, Long idUser){
        return requestRepository.findAllRequestByIdUser(idUser, pageable);
    }

    public Page<Request> readAllRequestByIdSuperior(Pageable pageable, Long id){
        List<User> users = userService.readUserByIdSuperior(
                id,
                new PageRequest(0, Integer.MAX_VALUE))
                    .getContent();
        List<Request> requests = new ArrayList<>();

        for (User user: users) {
            requests.addAll(requestRepository.findAllRequestByIdUser(user.getIdUser(),pageable)
                    .getContent());
        }
        return new PageImpl<>(requests, pageable, requests.size());
    }

    public Page<Request> readAllRequestByRequestStatus(
            Pageable pageable, RequestStatus requestStatus){
        return requestRepository.findAllRequestByStatus(requestStatus, pageable);
    }

    public Page<Request> readAllRequestByIdSuperiorAndRequestStatus(
            Pageable pageable, Long id, RequestStatus requestStatus){
        List<User> users = userService.readUserByIdSuperior(
                id,
                new PageRequest(0, Integer.MAX_VALUE))
                .getContent();
        List<Request> requests = new ArrayList<>();

        for (User user: users) {
            requests.addAll(requestRepository.findAllRequestByIdUser(user.getIdUser(),pageable)
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

        return ResponseEntity.ok().build(); }

}
