package com.future.assist.mapper;

import com.future.assist.model.entity_model.Request;
import com.future.assist.model.response_model.PageResponse;
import com.future.assist.model.response_model.ReqResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ReqMapper {

    @Autowired
    ItemMapper itemMapper;

    @Autowired
    UserMapper userMapper;

    public ReqResponse entityToResponse(Request request) {
        ReqResponse response = new ReqResponse();
        response.setIdRequest(request.getIdRequest());
        response.setRequestBy(userMapper.entityToResponse(request.getRequestBy()));
        response.setItem(itemMapper.entityToResponse(request.getItem()));
        response.setRequestDate(request.getRequestDate());
        response.setReqQty(request.getReqQty());
        response.setRequestStatus(request.getRequestStatus());
        response.setApprovedBy(request.getApprovedBy());
        response.setApprovedDate(request.getApprovedDate());
        response.setRejectedBy(request.getRejectedBy());
        response.setRejectedDate(request.getRejectedDate());
        response.setHandedOverBy(request.getHandedOverBy());
        response.setHandedOverDate(request.getHandedOverDate());
        response.setReturnedBy(request.getReturnedBy());
        response.setReturnedDate(request.getReturnedDate());
        return response;
    }

    public PageResponse<ReqResponse> pageToPageResponse(Page<Request> requests) {
        PageResponse<ReqResponse> response = new PageResponse<>();
        response.setNumber(requests.getNumber());
        response.setTotalPages(requests.getTotalPages());
        response.setSize(requests.getSize());
        response.setTotalElements(requests.getTotalElements());
        response.setNumberOfElements(requests.getNumberOfElements());
        response.setLast(requests.isLast());
        response.setFirst(requests.isFirst());
        List<ReqResponse> content = new ArrayList<>();
        for (Request request : requests.getContent()) {
            content.add(entityToResponse(request));
        }
        response.setContent(content);
        return response;
    }

}
