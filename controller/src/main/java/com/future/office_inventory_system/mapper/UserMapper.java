package com.future.office_inventory_system.mapper;

import com.future.office_inventory_system.model.entity_model.User;
import com.future.office_inventory_system.model.request_model.user.UserCreateRequest;
import com.future.office_inventory_system.model.request_model.user.UserUpdateRequest;
import com.future.office_inventory_system.model.response_model.PageResponse;
import com.future.office_inventory_system.model.response_model.SuperiorResponseModel;
import com.future.office_inventory_system.model.response_model.UserResponseModel;
import com.future.office_inventory_system.service.service_interface.UserService;
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

    public UserResponseModel entityToResponseModel(User user) {
        UserResponseModel responseModel = new UserResponseModel();
        responseModel.setDivision(user.getDivision());
        responseModel.setIdUser(user.getIdUser());
        responseModel.setIsActive(user.getIsActive());
        responseModel.setIsAdmin(user.getIsAdmin());
        responseModel.setName(user.getName());
        responseModel.setPictureURL(user.getPictureURL());
        responseModel.setRole(user.getRole());
        responseModel.setUsername(user.getUsername());
        if (user.getSuperior() != null) {
            SuperiorResponseModel superiorResponseModel = new SuperiorResponseModel();
            superiorResponseModel.setDivision(user.getSuperior().getDivision());
            superiorResponseModel.setIdUser(user.getSuperior().getIdUser());
            superiorResponseModel.setIsActive(user.getSuperior().getIsActive());
            superiorResponseModel.setIsAdmin(user.getSuperior().getIsAdmin());
            superiorResponseModel.setName(user.getSuperior().getName());
            superiorResponseModel.setPictureURL(user.getSuperior().getPictureURL());
            superiorResponseModel.setRole(user.getSuperior().getRole());
            superiorResponseModel.setUsername(user.getSuperior().getUsername());
            responseModel.setSuperior(superiorResponseModel);
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

    public PageResponse<UserResponseModel> pageToPageResponse(Page<User> users) {
        PageResponse<UserResponseModel> pageResponse = new PageResponse<>();
        List<UserResponseModel> content = new ArrayList<>();
        for (User user : users.getContent()) {
            content.add(entityToResponseModel(user));
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
