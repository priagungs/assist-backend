package com.future.assist.service.service_interface;

import com.future.assist.model.entity_model.UserHasItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface UserHasItemService {

    UserHasItem createUserHasItem(UserHasItem userHasItem);

    UserHasItem readUserHasItemById(Long id);

    ResponseEntity deleteUserHasItem(Long id);

    UserHasItem updateUserHasItem(UserHasItem userHasItem);

    Page<UserHasItem> readAllUserHasItems(Pageable pageable);

    Page<UserHasItem> readAllUserHasItemsByIdUser(Long idUser, Pageable pageable);

    Page<UserHasItem> readAllUserHasItemsByIdItem(Long idItem, Pageable pageable);

    UserHasItem createUserHasItemFromRequest(UserHasItem userHasItem);

    UserHasItem updateUserHasItemFromRequest(UserHasItem userHasItem);

}
