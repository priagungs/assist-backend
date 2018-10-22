package com.future.office_inventory_system.service;

import com.future.office_inventory_system.exception.NotFoundException;
import com.future.office_inventory_system.model.Item;
import com.future.office_inventory_system.model.Request;
import com.future.office_inventory_system.model.RequestStatus;
import com.future.office_inventory_system.model.User;
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

    public Request createRequest(Request request){

        User user= userService.readUserByIdUser(request.getRequestBy().getIdUser());

        if(user == null){
            throw new NotFoundException("user not found");
        }

        Item item= itemService
                .readItemByIdItem(request.getItem().getIdItem());

        if(item == null){
            throw new NotFoundException("item not found");
        }

        requestRepository.save(request);
        return request;
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
        return requestRepository.findAllByRequestBy(user, pageable);
    }

    public Page<Request> readAllRequestBySuperior(Pageable pageable, User superior){
        List<User> users = userService.readAllUsersByIdSuperior(
                superior.getIdUser(),
                new PageRequest(0, Integer.MAX_VALUE))
                .getContent();
        List<Request> requests = new ArrayList<>();

        for (User user: users) {
            requests.addAll(requestRepository.findAllByRequestBy(user,pageable)
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
                new PageRequest(0, Integer.MAX_VALUE))
                .getContent();
        List<Request> requests = new ArrayList<>();

        for (User user: users) {
            requests.addAll(requestRepository.findAllByRequestBy(user,pageable)
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
