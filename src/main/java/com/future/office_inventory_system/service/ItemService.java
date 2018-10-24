package com.future.office_inventory_system.service;

import com.future.office_inventory_system.model.Item;
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
  
  Page<Item> readItemsByAvailableGreaterThan(Integer min, Pageable pageable);
  
  ResponseEntity deleteItem(Long id);
  
}
