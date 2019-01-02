package com.future.assist.service.service_impl;

import com.future.assist.exception.InvalidValueException;
import com.future.assist.exception.NotFoundException;
import com.future.assist.model.RequestStatus;
import com.future.assist.model.entity_model.Item;
import com.future.assist.model.entity_model.Request;
import com.future.assist.model.entity_model.User;
import com.future.assist.model.entity_model.UserHasItem;
import com.future.assist.model.request_model.request.ReqCreateRequest;
import com.future.assist.model.request_model.request.ReqItemCreateRequest;
import com.future.assist.model.request_model.request.ReqUpdateRequest;
import com.future.assist.repository.RequestRepository;
import com.future.assist.service.service_interface.ItemService;
import com.future.assist.service.service_interface.RequestService;
import com.future.assist.service.service_interface.UserHasItemService;
import com.future.assist.service.service_interface.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class RequestServiceImpl implements RequestService {
    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserHasItemService userHasItemService;

    @Transactional
    public List<Request> createRequest(ReqCreateRequest requestBody) {
        User user = userService.readUserByIdUser(requestBody.getIdUser());
        List<Request> requests = new ArrayList<>();

        for (ReqItemCreateRequest el : requestBody.getItems()) {
            Item item = itemService.readItemByIdItem(el.getItem().getIdItem());
            Request request = new Request();

            request.setRequestBy(user);
            request.setItem(item);
            request.setRequestDate(new Date());
            if (item.getAvailableQty() >= el.getRequestQty()) {
                item.setAvailableQty(item.getAvailableQty() - el.getRequestQty());
                itemService.updateItem(item);
            } else {
                throw new InvalidValueException(el.getItem().getItemName() + " out of stock");
            }

            request.setReqQty(el.getRequestQty());

            if (user.getSuperior() == null) {
                request.setRequestStatus(RequestStatus.APPROVED);
            } else {
                request.setRequestStatus(RequestStatus.REQUESTED);
            }

            requests.add(requestRepository.save(request));
        }

        return requests;
    }

    @Transactional
    public Request updateRequest(ReqUpdateRequest reqUpdateRequest) {
        Request request = requestRepository.findRequestByIdRequest(reqUpdateRequest.getIdRequest())
                .orElseThrow(() -> new NotFoundException("request not found"));

        if (reqUpdateRequest.getRequestStatus() == RequestStatus.APPROVED &&
                request.getRequestStatus() == RequestStatus.REQUESTED) {
            if (userService.readUserByIdUser(reqUpdateRequest.getIdSuperior()) == null) {
                throw new NotFoundException("Superior not found");
            }
            request.setApprovedBy(reqUpdateRequest.getIdSuperior());
            request.setApprovedDate(new Date());

        } else if (reqUpdateRequest.getRequestStatus() == RequestStatus.REJECTED &&
                request.getRequestStatus() == RequestStatus.REQUESTED) {
            if (userService.readUserByIdUser(reqUpdateRequest.getIdSuperior()) == null) {
                throw new NotFoundException("Superior not found");
            }
            request.setRejectedBy(reqUpdateRequest.getIdSuperior());
            request.setRequestDate(new Date());
            Item item = itemService.readItemByIdItem(request.getItem().getIdItem());
            item.setAvailableQty(request.getReqQty() + item.getAvailableQty());
            itemService.updateItem(item);

        } else if (reqUpdateRequest.getRequestStatus() == RequestStatus.SENT &&
                request.getRequestStatus() == RequestStatus.APPROVED) {

            if (userService.readUserByIdUser(reqUpdateRequest.getIdAdmin()) == null) {
                throw new NotFoundException("Admin not found");
            }
            request.setHandedOverBy(reqUpdateRequest.getIdAdmin());
            request.setHandedOverDate(new Date());

            UserHasItem userHasItem = new UserHasItem();
            userHasItem.setUser(request.getRequestBy());
            userHasItem.setItem(request.getItem());
            userHasItem.setHasQty(request.getReqQty());
            userHasItemService.createUserHasItemFromRequest(userHasItem);
        } else {
            throw new InvalidValueException("Invalid input");
        }
        request.setRequestStatus(reqUpdateRequest.getRequestStatus());

        return requestRepository.save(request);
    }

    @Transactional
    public Request updateRequestByRequestObject(Request request) {
        Request beforeUpdate = requestRepository.findById(request.getIdRequest())
                .orElseThrow(() -> new NotFoundException("request not found"));
        if (request.getRequestStatus() == RequestStatus.REJECTED) {
            Item item = itemService.readItemByIdItem(request.getItem().getIdItem());
            item.setAvailableQty(request.getReqQty() + item.getAvailableQty());
            itemService.updateItem(item);
        } else if (request.getRequestStatus() == RequestStatus.RETURNED) {
            List<UserHasItem> userHasItems = beforeUpdate.getRequestBy().getHasItems();
            for (UserHasItem userHasItem : userHasItems) {
                if (userHasItem.getItem().getIdItem() == beforeUpdate.getItem().getIdItem()) {
                    userHasItemService.deleteUserHasItem(userHasItem.getIdUserHasItem());
                }
            }
        }
        beforeUpdate.setRejectedBy(request.getRejectedBy());
        beforeUpdate.setRejectedDate(request.getRejectedDate());
        beforeUpdate.setRequestStatus(request.getRequestStatus());
        beforeUpdate.setReturnedBy(request.getReturnedBy());
        beforeUpdate.setReturnedDate(request.getReturnedDate());

        return requestRepository.save(beforeUpdate);
    }

    @Transactional
    public Request updateRequestStatusToReturned(Request request) {
        Request updated = requestRepository.findById(request.getIdRequest())
                .orElseThrow(() -> new NotFoundException("Request not found"));
        updated.setRequestStatus(RequestStatus.RETURNED);
        updated.setReturnedDate(request.getReturnedDate());
        updated.setReturnedBy(request.getReturnedBy());
        return requestRepository.save(updated);
    }

    public List<Request> readAllRequestsByItem(Item item) {
        return requestRepository.findAllRequestsByItem(item);
    }

    public Page<Request> readAllRequest(Pageable pageable) {
        return requestRepository.findAll(pageable);
    }

    public Page<Request> readRequestByUser(Pageable pageable, User user) {
        return requestRepository.findAllRequestsByRequestBy(user, pageable);
    }

    @Transactional
    public Page<Request> readAllRequestBySuperior(Pageable pageable, User superior) {
        List<User> users = userService.readAllUsersByIdSuperior(
                superior.getIdUser(),
                PageRequest.of(0, Integer.MAX_VALUE))
                .getContent();
        List<Request> requests = new ArrayList<>();

        for (User user : users) {
            requests.addAll(requestRepository.findAllRequestsByRequestBy(user, pageable)
                    .getContent());
        }
        return new PageImpl<>(requests, pageable, requests.size());
    }

    public Page<Request> readAllRequestByRequestStatus(Pageable pageable, RequestStatus requestStatus) {
        return requestRepository.findAllRequestsByRequestStatus(requestStatus, pageable);
    }

    @Transactional
    public Page<Request> readAllRequestBySuperiorAndRequestStatus(
            Pageable pageable, User superior, RequestStatus requestStatus) {
        List<User> users = userService.readAllUsersByIdSuperior(
                superior.getIdUser(),
                PageRequest.of(0, Integer.MAX_VALUE))
                .getContent();
        List<Request> requests = new ArrayList<>();

        for (User user : users) {
            requests.addAll(requestRepository.findAllRequestsByRequestBy(user, pageable)
                    .getContent());
        }

        List<Request> reqs = new ArrayList<>();

        for (Request request : requests) {
            if (request.getRequestStatus() == requestStatus) {
                reqs.add(request);
            }
        }

        return new PageImpl<>(reqs, pageable, reqs.size());
    }

    public Page<Request> readAllRequestByUserAndStatus(Pageable pageable, User user, RequestStatus requestStatus) {
        return requestRepository.findAllByRequestByAndRequestStatus(user, requestStatus, pageable);
    }

    @Transactional
    public ResponseEntity deleteRequest(Request req) {
        Request request = requestRepository.findById(req.getIdRequest()).orElseThrow(
                () -> new NotFoundException("Request not found"));
        if (request.getRequestStatus() == RequestStatus.REQUESTED ||
                request.getRequestStatus() == RequestStatus.APPROVED) {
            Item item = itemService.readItemByIdItem(request.getItem().getIdItem());
            item.setAvailableQty(request.getReqQty() + item.getAvailableQty());
            itemService.updateItem(item);
        }

        requestRepository.delete(request);

        return ResponseEntity.ok().build();
    }

    public Request readRequestByIdRequest(Long id) {
        return requestRepository.findById(id).orElseThrow(() -> new NotFoundException("Request not found"));
    }
}
