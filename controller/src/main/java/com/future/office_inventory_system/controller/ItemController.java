package com.future.office_inventory_system.controller;

import com.future.office_inventory_system.exception.UnauthorizedException;
import com.future.office_inventory_system.model.entity_model.Item;
import com.future.office_inventory_system.model.request_body_model.item.ItemCreateUpdateRequest;
import com.future.office_inventory_system.model.request_body_model.item.ItemModelRequest;
import com.future.office_inventory_system.service.service_impl.LoggedinUserInfo;
import com.future.office_inventory_system.service.service_interface.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ItemController {

    @Autowired
    ItemService itemService;

    @Autowired
    LoggedinUserInfo loggedinUserInfo;

    @PostMapping("/items")
    public List<Item> createItem(@RequestBody List<ItemCreateUpdateRequest> itemRequests) {
        if (!loggedinUserInfo.getUser().getIsAdmin()) {
            throw new UnauthorizedException("You are not an admin");
        }

        List<Item> items = new ArrayList<>();
        for (ItemCreateUpdateRequest itemRequest : itemRequests) {
            Item item = new Item();
            item.setItemName(itemRequest.getItemName());
            item.setDescription(itemRequest.getDescription());
            item.setPictureURL(itemRequest.getPictureURL());
            item.setPrice(itemRequest.getPrice());
            item.setTotalQty(itemRequest.getTotalQty());
            items.add(item);
        }

        List<Item> result = new ArrayList<>();
        for (Item item : items) {
            result.add(itemService.createItem(item));
        }
        return result;
    }

    @GetMapping("/items")
    public Page<Item> readAllItems(@RequestParam("page") Integer page,
                                   @RequestParam("limit") Integer limit,
                                   @RequestParam(value = "keyword", required = false) String keyword,
                                   @RequestParam("sort") String sort,
                                   @RequestParam(value = "minqty", required = false) Integer minqty) {

        if (loggedinUserInfo.getUser().getIsAdmin()) {
            if (keyword != null) {
                if (minqty != null) {
                    return itemService.readAllItemsByKeywordAndAvailableGreaterThan(keyword, minqty,
                            PageRequest.of(page, limit, Sort.Direction.ASC, sort));
                } else {
                    return itemService.readAllItemsContaining(keyword,
                            PageRequest.of(page, limit, Sort.Direction.ASC, sort));
                }
            } else {
                if (minqty != null) {
                    return itemService.readItemsByAvailableGreaterThan(minqty,
                            PageRequest.of(page, limit, Sort.Direction.ASC, sort));
                } else {
                    return itemService.readAllItems(
                            PageRequest.of(page, limit, Sort.Direction.ASC, sort));
                }
            }
        } else {
            if (keyword != null) {
                return itemService.readAllItemsByKeywordAndAvailableGreaterThan(keyword, 0,
                        PageRequest.of(page, limit, Sort.Direction.ASC, sort));
            } else {
                return itemService.readItemsByAvailableGreaterThan(0,
                        PageRequest.of(page, limit, Sort.Direction.ASC, sort));
            }
        }
    }


    @GetMapping("/items/{idItem}")
    public Item readItemByIdItem(@PathVariable("idItem") Long idItem) {
        return itemService.readItemByIdItem(idItem);
    }

    @GetMapping("/item")
    public Item readItemByItemName(@RequestParam("name") String name) {
        if (!loggedinUserInfo.getUser().getIsAdmin()) {
            throw new UnauthorizedException("you are not an admin");
        }
        return itemService.readItemByItemName(name);
    }

    @PutMapping("/items/{idItem}")
    public Item updateItem(@RequestBody ItemCreateUpdateRequest itemRequest, @PathVariable("idItem") Long id) {
        if (!loggedinUserInfo.getUser().getIsAdmin()) {
            throw new UnauthorizedException("You are not permitted to update this item");
        }
        Item item = new Item();
        item.setItemName(itemRequest.getItemName());
        item.setDescription(itemRequest.getDescription());
        item.setPictureURL(itemRequest.getPictureURL());
        item.setPrice(itemRequest.getPrice());
        item.setTotalQty(itemRequest.getTotalQty());
        item.setIdItem(id);
        return itemService.updateItem(item);
    }

    @DeleteMapping("/items")
    public ResponseEntity deleteItem(@RequestBody ItemModelRequest item) {
        if (!loggedinUserInfo.getUser().getIsAdmin()) {
            throw new UnauthorizedException("You are not permitted to delete this item");
        }
        return itemService.deleteItem(item.getIdItem());
    }

}
