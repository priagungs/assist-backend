package com.future.office_inventory_system.controller;

import com.future.office_inventory_system.exception.UnauthorizedException;
import com.future.office_inventory_system.model.entity_model.UserHasItem;
import com.future.office_inventory_system.model.request_body_model.user.UserHasItemModelRequest;
import com.future.office_inventory_system.service.service_impl.LoggedinUserInfo;
import com.future.office_inventory_system.service.service_interface.UserHasItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserHasItemController {

    @Autowired
    UserHasItemService userHasItemService;

    @Autowired
    LoggedinUserInfo loggedinUserInfo;

    @GetMapping("/user-items")
    Page<UserHasItem> readUserHasItems(@RequestParam("page") Integer page, @RequestParam("limit") Integer limit,
                                       @RequestParam(value = "idUser", required = false) Long idUser,
                                       @RequestParam(value = "idItem", required = false) Long idItem,
                                       @RequestParam("sort") String sort) {
        if (idUser != null) {
            return userHasItemService.readAllUserHasItemsByIdUser(idUser, PageRequest.of(page, limit, Sort.Direction.ASC, sort));
        } else if (idItem != null) {
            return userHasItemService.readAllUserHasItemsByIdItem(idItem, PageRequest.of(page, limit, Sort.Direction.ASC, sort));
        } else {
            return userHasItemService.readAllUserHasItems(PageRequest.of(page, limit, Sort.Direction.ASC, sort));
        }
    }

    @GetMapping("/user-items/{id}")
    UserHasItem readUserHasItem(@PathVariable("id") Long id) {
        return userHasItemService.readUserHasItemById(id);
    }

    @DeleteMapping("/user-items")
    ResponseEntity deleteUserHasItem(@RequestBody UserHasItemModelRequest userHasItem) {
        Long idUser = userHasItemService.readUserHasItemById(userHasItem.getIdUserHasItem()).getUser().getIdUser();
        if (!loggedinUserInfo.getUser().getIsAdmin() && loggedinUserInfo.getUser().getIdUser() != idUser) {
            throw new UnauthorizedException("you are not permitted to create userhasitem");
        }
        return userHasItemService.deleteUserHasItem(userHasItem.getIdUserHasItem());
    }
}
