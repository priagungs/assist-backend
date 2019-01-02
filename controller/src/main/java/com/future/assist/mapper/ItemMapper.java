package com.future.assist.mapper;

import com.future.assist.model.entity_model.Item;
import com.future.assist.model.request_model.item.ItemCreateUpdateRequest;
import com.future.assist.model.response_model.ItemResponse;
import com.future.assist.model.response_model.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ItemMapper {
    public Item requestToEntity(ItemCreateUpdateRequest itemRequest) {
        Item item = new Item();
        item.setItemName(itemRequest.getItemName());
        item.setDescription(itemRequest.getDescription());
        item.setPictureURL(itemRequest.getPictureURL());
        item.setPrice(itemRequest.getPrice());
        item.setTotalQty(itemRequest.getTotalQty());
        return item;
    }

    public ItemResponse entityToResponse(Item item) {
        ItemResponse responseModel = new ItemResponse();
        responseModel.setAvailableQty(item.getAvailableQty());
        responseModel.setDescription(item.getDescription());
        responseModel.setIdItem(item.getIdItem());
        responseModel.setIsActive(item.getIsActive());
        responseModel.setItemName(item.getItemName());
        responseModel.setPictureURL(item.getPictureURL());
        responseModel.setPrice(item.getPrice());
        responseModel.setTotalQty(item.getTotalQty());
        return responseModel;
    }

    public PageResponse<ItemResponse> pageToPageResponse(Page<Item> items) {
        PageResponse<ItemResponse> pagedItems = new PageResponse<>();
        pagedItems.setTotalPages(items.getTotalPages());
        pagedItems.setSize(items.getSize());
        pagedItems.setNumberOfElements(items.getNumberOfElements());
        pagedItems.setLast(items.isLast());
        pagedItems.setFirst(items.isFirst());
        pagedItems.setTotalElements(items.getTotalElements());
        List<ItemResponse> content = new ArrayList<>();
        for (Item item : items.getContent()) {
            content.add(entityToResponse(item));
        }
        pagedItems.setContent(content);
        return pagedItems;
    }
}
