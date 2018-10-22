package com.future.office_inventory_system.service;

import com.future.office_inventory_system.exception.ConflictException;
import com.future.office_inventory_system.exception.InvalidValueException;
import com.future.office_inventory_system.exception.NotFoundException;
import com.future.office_inventory_system.model.Item;
import com.future.office_inventory_system.model.User;
import com.future.office_inventory_system.model.UserHasItem;
import com.future.office_inventory_system.repository.UserHasItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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
        User user = userService.readUserByIdUser(userHasItem.getUser().getIdUser());
        Item item = itemService.readItemByIdItem(userHasItem.getItem().getIdItem());

        if (repository.findAllByUserAndItem(user, item).size() > 0) {
            return updateUserHasItem(userHasItem);
        }
        else {
            if (userHasItem.getHasQty() > item.getAvailableQty()) {
                throw new InvalidValueException("item available quantity is not sufficient");
            }
            userHasItem.setUser(user);
            userHasItem.setItem(item);
            return repository.save(userHasItem);
        }
    }

    public UserHasItem readUserHasItemById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("userhasitem not found"));
    }

    public ResponseEntity deleteUserHasItem(Long id) {
        UserHasItem hasItem = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("usehasitem not found"));
        Item item = hasItem.getItem();
        item.setAvailableQty(item.getAvailableQty() + hasItem.getHasQty());
        itemService.updateItem(item);

        repository.delete(hasItem);
        return ResponseEntity.ok().build();

    }

    public UserHasItem updateUserHasItem(UserHasItem userHasItem) {
        if (!repository.findById(userHasItem.getIdUserHasItem()).isPresent()) {
            throw new NotFoundException("userhasitem not found");
        }
        UserHasItem beforeUpdate = repository.findById(userHasItem.getIdUserHasItem()).get();
        if (userHasItem.getHasQty() - beforeUpdate.getHasQty() >
            itemService.readItemByIdItem(userHasItem.getItem().getIdItem())
                    .getAvailableQty()) {
            throw new InvalidValueException("item available quantity is not sufficient");
        }

        userHasItem.setUser(userService.readUserByIdUser(userHasItem.getUser().getIdUser()));
        userHasItem.setItem(itemService.readItemByIdItem(userHasItem.getItem().getIdItem()));
        return repository.save(userHasItem);
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
