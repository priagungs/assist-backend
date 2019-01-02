package com.future.assist.mapper;

import com.future.assist.model.entity_model.UserHasItem;
import com.future.assist.model.response_model.PageResponse;
import com.future.assist.model.response_model.UserHasItemResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserHasItemMapper {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ItemMapper itemMapper;

    public UserHasItemResponse entityToResponse(UserHasItem userHasItem) {
        UserHasItemResponse response = new UserHasItemResponse();
        response.setIdUserHasItem(userHasItem.getIdUserHasItem());
        response.setHasQty(userHasItem.getHasQty());
        response.setItem(itemMapper.entityToResponse(userHasItem.getItem()));
        response.setUser(userMapper.entityToResponse(userHasItem.getUser()));
        return response;
    }

    public PageResponse<UserHasItemResponse> pageToPageResponse(Page<UserHasItem> userHasItems) {
        PageResponse<UserHasItemResponse> response = new PageResponse<>();
        response.setTotalElements(userHasItems.getTotalElements());
        response.setTotalPages(userHasItems.getTotalPages());
        response.setLast(userHasItems.isLast());
        response.setNumber(userHasItems.getNumber());
        response.setSize(userHasItems.getSize());
        response.setNumberOfElements(userHasItems.getNumberOfElements());
        response.setFirst(userHasItems.isFirst());
        List<UserHasItemResponse> content = new ArrayList<>();
        for (UserHasItem userHasItem : userHasItems.getContent()) {
            content.add(entityToResponse(userHasItem));
        }
        response.setContent(content);
        return response;
    }
}
