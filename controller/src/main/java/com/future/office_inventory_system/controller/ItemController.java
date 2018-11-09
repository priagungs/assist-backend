package com.future.office_inventory_system.controller;

import com.future.office_inventory_system.exception.UnauthorizedException;
import com.future.office_inventory_system.model.Item;
import com.future.office_inventory_system.service.ItemService;
import com.future.office_inventory_system.value_object.LoggedinUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api")
public class ItemController {
    
    @Autowired
    ItemService itemService;
    
    @Autowired
    LoggedinUserInfo loggedinUserInfo;
    
    @PostMapping("/items")
    public List<Item> createItem(@RequestBody List<Item> items) {
        if (!loggedinUserInfo.getUser().getIsAdmin()) {
            throw new UnauthorizedException("You are not an admin");
        }
        
        List<Item> result = new ArrayList<>();
        for (Item item : items) {
            result.add(itemService.createItem(item));
        }
        return result;
    }
    
    @GetMapping("/items")
    public Page<Item> readAllUsers(@RequestParam("page") Integer page,
                                   @RequestParam("limit") Integer limit) {
        
        if (loggedinUserInfo.getUser().getIsAdmin()) {
            return itemService.readAllItems(PageRequest.of(page, limit));
        } else {
            return itemService.readItemsByAvailableGreaterThan(0, PageRequest.of(page, limit));
        }
    }
    
    @GetMapping("items/{idItem}")
    public Item readItemByIdItem(@PathVariable("idItem") Long idItem) {
        return itemService.readItemByIdItem(idItem);
    }
    
    @PutMapping("/items/{idItem}")
    public Item updateItem(@RequestBody Item item, @PathVariable("idItem") Long id) {
        if (!loggedinUserInfo.getUser().getIsAdmin()) {
            throw new UnauthorizedException("You are not permitted to update this item");
        }
        item.setIdItem(id);
        return itemService.updateItem(item);
    }
    
    @DeleteMapping("/items")
    public ResponseEntity deleteItem(@RequestBody Item item) {
        if (!loggedinUserInfo.getUser().getIsAdmin()) {
            throw new UnauthorizedException("You are not permitted to delete this item");
        }
        return itemService.deleteItem(item.getIdItem());
    }
    
}
