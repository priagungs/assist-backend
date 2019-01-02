package com.future.assist.controller;

import com.future.assist.exception.UnauthorizedException;
import com.future.assist.mapper.ItemMapper;
import com.future.assist.model.entity_model.Item;
import com.future.assist.model.request_model.item.ItemCreateUpdateRequest;
import com.future.assist.model.request_model.item.ItemModelRequest;
import com.future.assist.model.response_model.ItemResponse;
import com.future.assist.model.response_model.PageResponse;
import com.future.assist.service.service_impl.LoggedinUserInfo;
import com.future.assist.service.service_interface.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
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
    private ItemService itemService;

    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private LoggedinUserInfo loggedinUserInfo;

    @PostMapping("/items")
    public List<ItemResponse> createItem(@RequestBody List<ItemCreateUpdateRequest> itemRequests) {
        if (!loggedinUserInfo.getUser().getIsAdmin()) {
            throw new UnauthorizedException("You are not an admin");
        }

        List<Item> items = new ArrayList<>();
        for (ItemCreateUpdateRequest itemRequest : itemRequests) {
            items.add(itemMapper.requestToEntity(itemRequest));
        }

        List<ItemResponse> result = new ArrayList<>();
        for (Item item : items) {
            result.add(itemMapper.entityToResponse(itemService.createItem(item)));
        }
        return result;
    }

    @GetMapping("/items")
    public PageResponse<ItemResponse> readAllItems(@RequestParam("page") Integer page,
                                                   @RequestParam("limit") Integer limit,
                                                   @RequestParam(value = "keyword", required = false) String keyword,
                                                   @RequestParam("sort") String sort,
                                                   @RequestParam(value = "minqty", required = false) Integer minqty) {
        if (loggedinUserInfo.getUser().getIsAdmin()) {
            if (keyword != null) {
                if (minqty != null) {
                    return itemMapper.pageToPageResponse(itemService.readAllItemsByKeywordAndAvailableGreaterThan(keyword, minqty,
                            PageRequest.of(page, limit, Sort.Direction.ASC, sort)));
                } else {
                    return itemMapper.pageToPageResponse(itemService.readAllItemsContaining(keyword,
                            PageRequest.of(page, limit, Sort.Direction.ASC, sort)));
                }
            } else {
                if (minqty != null) {
                    return itemMapper.pageToPageResponse(itemService.readItemsByAvailableGreaterThan(minqty,
                            PageRequest.of(page, limit, Sort.Direction.ASC, sort)));
                } else {
                    return itemMapper.pageToPageResponse(itemService.readAllItems(PageRequest
                            .of(page, limit, Sort.Direction.ASC, sort)));
                }
            }
        } else {
            if (keyword != null) {
                return itemMapper.pageToPageResponse(itemService.readAllItemsByKeywordAndAvailableGreaterThan(keyword, 0,
                        PageRequest.of(page, limit, Sort.Direction.ASC, sort)));
            } else {
                return itemMapper.pageToPageResponse(itemService.readItemsByAvailableGreaterThan(0,
                        PageRequest.of(page, limit, Sort.Direction.ASC, sort)));
            }
        }
    }

    @GetMapping("/items/{idItem}")
    public ItemResponse readItemByIdItem(@PathVariable("idItem") Long idItem) {
        return itemMapper.entityToResponse(itemService.readItemByIdItem(idItem));
    }

    @GetMapping("/item")
    public ItemResponse readItemByItemName(@RequestParam("name") String name) {
        if (!loggedinUserInfo.getUser().getIsAdmin()) {
            throw new UnauthorizedException("you are not an admin");
        }
        return itemMapper.entityToResponse(itemService.readItemByItemName(name));
    }

    @PutMapping("/items/{idItem}")
    public ItemResponse updateItem(@RequestBody ItemCreateUpdateRequest itemRequest, @PathVariable("idItem") Long id) {
        if (!loggedinUserInfo.getUser().getIsAdmin()) {
            throw new UnauthorizedException("You are not permitted to update this item");
        }
        return itemMapper.entityToResponse(itemService
                .updateItem(itemMapper.requestToEntity(itemRequest)));
    }

    @DeleteMapping("/items")
    public ResponseEntity deleteItem(@RequestBody ItemModelRequest item) {
        if (!loggedinUserInfo.getUser().getIsAdmin()) {
            throw new UnauthorizedException("You are not permitted to delete this item");
        }
        return itemService.deleteItem(item.getIdItem());
    }
}
