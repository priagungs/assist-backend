package com.future.office_inventory_system.service;

import com.future.office_inventory_system.model.Item;
import com.future.office_inventory_system.model.User;
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
            throw new RuntimeException("userhasitem already present");
        }

        userHasItem.setUser(userService.readUserByIdUser(userHasItem.getUser().getIdUser()));
        userHasItem.setItem(itemService.readItemByIdItem(userHasItem.getItem().getIdItem()));
        return repository.save(userHasItem);
    }

    public UserHasItem readUserHasItemById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("userhasitem not found"));
    }

    public ResponseEntity deleteUserHasItem(Long id) {
        UserHasItem hasItem = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("usehasitem not found"));
    }

    public ResponseEntity updateUserHasItem(UserHasItem userHasItem) {
        return null;
    }

    public Page<UserHasItem> readAllUserHasItems(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<UserHasItem> readAllUserHasItemsByIdUser(Long idUser, Pageable pageable) {
        User user = userService.readUserByIdUser(idUser);
        return repository.findAllByUser(user, pageable);
    }

    public Page<UserHasItem> readAllUserHasItemsByIdItem(Long idItem, Pageable pageable) {
        Item item = itemService.readItemByIdItem(idItem);
        return repository.findAllByItem(item, pageable);
    }
}
