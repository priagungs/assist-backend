package com.future.office_inventory_system.service;

import com.future.office_inventory_system.exception.NotFoundException;
import com.future.office_inventory_system.model.Item;
import com.future.office_inventory_system.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
public class ItemServiceImpl implements ItemService {
  
  @Autowired
  private ItemRepository itemRepository;
  
  public ResponseEntity createItem(Item item) {
    
    if (itemRepository.findByItemName(item.getItemName()).isPresent()) {
      throw new RuntimeException();
    }
    
    itemRepository.save(item);
    return ResponseEntity.ok().build();
    
  }
  
  public ResponseEntity updateItem(Item item) {
    Item itemBefore = itemRepository
      .findById(item.getIdItem())
      .orElseThrow(() -> new NotFoundException("Item not found"));
    
    if (item.getTotalQty() < 0 || item.getAvailableQty() > item.getTotalQty() ||
      item.getAvailableQty() < 0  || item.getPrice() < 0) {
      //exception  invalid value
    }
    
    itemBefore.setItemName(item.getItemName());
    itemBefore.setPictureURL(item.getPictureURL());
    itemBefore.setPrice(item.getPrice());
    itemBefore.setTotalQty(item.getTotalQty());
    itemBefore.setAvailableQty(item.getAvailableQty());
    itemBefore.setDescription(item.getDescription());
    itemBefore.setActive(item.getActive());
    
    itemRepository.save(itemBefore);
    return ResponseEntity.ok().build();
    
  }
  
  public Page<Item> readAllItem(Pageable pageable) {
    return itemRepository.findAll(pageable);
  }
  
  public Item readItemByIdItem(Long id) {
    return itemRepository.findById(id)
      .orElseThrow(() -> new NotFoundException("Item not found"));
  }
  
  public Page<Item> readItemByAvailableGreaterThan(Integer min, Pageable pageable) {
    if (min < 0)
    return itemRepository.findAllByAvailableQtyGreaterThan(min,pageable);
  }
  
  public ResponseEntity deleteItem(Long id) {}

}
