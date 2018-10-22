package com.future.office_inventory_system.service;

import com.future.office_inventory_system.model.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
public interface ItemService {
  
    ResponseEntity createItem(Item item);
    
    ResponseEntity updateItem(Item item);
    
    Page<Item> readAllItem(Pageable pageable);
    
    Item readItemByIdItem(Long id);
    
    Page<Item> readItemByAvailableGreaterThan(Integer min, Pageable pageable);
    
    ResponseEntity deleteItem(Long id);
  
  
}
