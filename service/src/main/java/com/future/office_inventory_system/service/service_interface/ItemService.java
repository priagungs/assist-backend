package com.future.office_inventory_system.service.service_interface;

import com.future.office_inventory_system.model.entity_model.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
public interface ItemService {

    Item createItem(Item item);

    Item updateItem(Item item);

    Page<Item> readAllItems(Pageable pageable);

    Item readItemByIdItem(Long id);

    Item readItemByItemName(String name);

    Page<Item> readItemsByAvailableGreaterThan(Integer min, Pageable pageable);

    Page<Item> readAllItemsContaining(String keyword, Pageable pageable);

    Page<Item> readAllItemsByKeywordAndAvailableGreaterThan(String keyword, Integer min, Pageable pageable);

    ResponseEntity deleteItem(Long id);

}
