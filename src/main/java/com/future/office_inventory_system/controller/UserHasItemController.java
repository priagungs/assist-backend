package com.future.office_inventory_system.controller;

import com.future.office_inventory_system.exception.UnauthorizedException;
import com.future.office_inventory_system.model.UserHasItem;
import com.future.office_inventory_system.service.UserHasItemService;
import com.future.office_inventory_system.value_object.LoggedinUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserHasItemController {

    @Autowired
    UserHasItemService userHasItemService;

    @Autowired
    LoggedinUserInfo loggedinUserInfo;

    @PostMapping("/user-items")
    UserHasItem createUserHasItem(@RequestBody UserHasItem userHasItem) {
        if (!loggedinUserInfo.getUser().getIsAdmin()) {
            throw new UnauthorizedException("you are not permitted to create userhasitem");
        }
        return userHasItemService.createUserHasItem(userHasItem);
    }

    @GetMapping("/user-items")
    Page<UserHasItem> readUserHasItems(@RequestParam("page") Integer page, @RequestParam("limit") Integer limit,
                                          @RequestParam(value = "idUser", required = false) Long idUser,
                                          @RequestParam(value = "idItem", required = false) Long idItem) {
        if (idUser != null) {
            return userHasItemService.readAllUserHasItemsByIdUser(idUser, PageRequest.of(page, limit));
        }
        else if (idItem != null) {
            return userHasItemService.readAllUserHasItemsByIdItem(idItem, PageRequest.of(page, limit));
        }
        else {
            return userHasItemService.readAllUserHasItems(PageRequest.of(page, limit));
        }
    }

    @GetMapping("/user-items/{id}")
    UserHasItem readUserHasItem(@PathVariable("id") Long id) {
        return userHasItemService.readUserHasItemById(id);
    }

    @PutMapping("/user-items")
    UserHasItem updateUserHasItem(@RequestParam UserHasItem userHasItem) {
        if (!loggedinUserInfo.getUser().getIsAdmin()) {
            throw new UnauthorizedException("you are not permitted to create userhasitem");
        }
        return userHasItemService.updateUserHasItem(userHasItem);
    }

    @DeleteMapping("/user-items")
    ResponseEntity deleteUserHasItem(@RequestParam UserHasItem userHasItem) {
        if (!loggedinUserInfo.getUser().getIsAdmin()) {
            throw new UnauthorizedException("you are not permitted to create userhasitem");
        }
        return userHasItemService.deleteUserHasItem(userHasItem.getIdUserHasItem());
    }
}
