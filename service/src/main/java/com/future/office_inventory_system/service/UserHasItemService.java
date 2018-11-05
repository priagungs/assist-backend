package com.future.office_inventory_system.service;

import com.future.office_inventory_system.model.Item;
import com.future.office_inventory_system.model.User;
import com.future.office_inventory_system.model.UserHasItem;
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
