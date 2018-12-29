package com.future.assist.mapper;

import com.future.assist.model.entity_model.User;
import com.future.assist.model.request_model.user.UserCreateRequest;
import com.future.assist.model.request_model.user.UserUpdateRequest;
import com.future.assist.model.response_model.PageResponse;
import com.future.assist.model.response_model.SuperiorResponse;
import com.future.assist.model.response_model.UserResponse;
import com.future.assist.service.service_interface.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserMapper {

    @Autowired
    UserService userService;

    public User createRequestToEntity(UserCreateRequest userRequest) {
        User user = new User();
        user.setIsAdmin(userRequest.getIsAdmin());
        user.setName(userRequest.getName());
        user.setUsername(userRequest.getUsername());
        user.setPassword(userRequest.getPassword());
        user.setPictureURL(userRequest.getPictureURL());
        user.setDivision(userRequest.getDivision());
        user.setRole(userRequest.getRole());
        user.setSuperior(userService.readUserByIdUser(userRequest.getSuperior().getIdUser()));
        return user;
    }

    public UserResponse entityToResponse(User user) {
        UserResponse responseModel = new UserResponse();
        responseModel.setDivision(user.getDivision());
        responseModel.setIdUser(user.getIdUser());
        responseModel.setIsActive(user.getIsActive());
        responseModel.setIsAdmin(user.getIsAdmin());
        responseModel.setName(user.getName());
        responseModel.setPictureURL(user.getPictureURL());
        responseModel.setRole(user.getRole());
        responseModel.setUsername(user.getUsername());
        if (user.getSuperior() != null) {
            SuperiorResponse superiorResponse = new SuperiorResponse();
            superiorResponse.setDivision(user.getSuperior().getDivision());
            superiorResponse.setIdUser(user.getSuperior().getIdUser());
            superiorResponse.setIsActive(user.getSuperior().getIsActive());
            superiorResponse.setIsAdmin(user.getSuperior().getIsAdmin());
            superiorResponse.setName(user.getSuperior().getName());
            superiorResponse.setPictureURL(user.getSuperior().getPictureURL());
            superiorResponse.setRole(user.getSuperior().getRole());
            superiorResponse.setUsername(user.getSuperior().getUsername());
            responseModel.setSuperior(superiorResponse);
        }
        return responseModel;
    }

    public User updateRequestToEntity(UserUpdateRequest userRequest) {
        User user = new User();
        user.setIsAdmin(userRequest.getIsAdmin());
        user.setName(userRequest.getName());
        user.setUsername(userRequest.getUsername());
        user.setPassword(userRequest.getPassword());
        user.setPictureURL(userRequest.getPictureURL());
        user.setDivision(userRequest.getDivision());
        user.setRole(userRequest.getRole());
        user.setSuperior(userService.readUserByIdUser(userRequest.getSuperior().getIdUser()));
        user.setIdUser(userRequest.getIdUser());
        return user;
    }

    public PageResponse<UserResponse> pageToPageResponse(Page<User> users) {
        PageResponse<UserResponse> pageResponse = new PageResponse<>();
        List<UserResponse> content = new ArrayList<>();
        for (User user : users.getContent()) {
            content.add(entityToResponse(user));
        }
        pageResponse.setContent(content);
        pageResponse.setFirst(users.isFirst());
        pageResponse.setLast(users.isLast());
        pageResponse.setNumber(users.getNumber());
        pageResponse.setNumberOfElements(users.getNumberOfElements());
        pageResponse.setSize(users.getSize());
        pageResponse.setTotalElements(users.getTotalElements());
        pageResponse.setTotalPages(users.getTotalPages());
        return pageResponse;
    }

}
