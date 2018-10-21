package com.future.office_inventory_system.service;

import com.future.office_inventory_system.model.UserHasItem;
import com.future.office_inventory_system.repository.UserHasItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserHasItemServiceImpl implements UserHasItemService {

    @Autowired
    UserHasItemRepository repository;

    @Autowired
    ItemService itemService;

    @Autowired
    UserService userService;

    public UserHasItem createUserHasItem(UserHasItem userHasItem) {
        if (repository.findById(userHasItem.getIdUserHasItem()).isPresent()) {
            throw new RuntimeException("user has item already present");
        }

        userHasItem.setUser(userService.readUserByIdUser(userHasItem.getUser().getIdUser()));
        userHasItem.setItem(itemService.readItemByIdItem(userHasItem.getItem().getIdItem()));
        return repository.save(userHasItem);
    }

    public UserHasItem readUserHasItemById(Long id) {
        return null;
    }

    public ResponseEntity deleteUserHasItem(Long id) {
        return null;
    }

    public ResponseEntity updateUserHasItem(UserHasItem userHasItem) {
        return null;
    }

    public Page<UserHasItem> readAllUserHasItems(Pageable pageable) {
        return null;
    }

    public Page<UserHasItem> readAllUserHasItemsByIdUser(Long idUser, Pageable pageable) {
        return null;
    }

    public Page<UserHasItem> readAllUserHasItemsByIdItem(Long idItem, Pageable pageable) {
        return null;
    }
}
