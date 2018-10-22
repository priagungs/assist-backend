package com.future.office_inventory_system.service;

import com.future.office_inventory_system.exception.InvalidValueException;
import com.future.office_inventory_system.exception.NotFoundException;
import com.future.office_inventory_system.model.Item;
import com.future.office_inventory_system.model.User;
import com.future.office_inventory_system.model.UserHasItem;
import com.future.office_inventory_system.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ItemServiceImpl implements ItemService {
    
    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserHasItemService userHasItemService;
    
    public Item createItem(Item item) {
        if (itemRepository.findByItemName(item.getItemName()).isPresent()) {
            throw new RuntimeException(item.getItemName() + " is exist");
        }
        item.setAvailableQty(item.getTotalQty());
        return itemRepository.save(item);
    }
    
    public Item updateItem(Item item) {
        Item itemBefore = itemRepository
            .findById(item.getIdItem())
            .orElseThrow(() -> new NotFoundException("Item not found"));

        if (item.getTotalQty() < itemBefore.getTotalQty() - itemBefore.getAvailableQty()) {
            throw new InvalidValueException("Invalid value");
        }

        itemBefore.setItemName(item.getItemName());
        itemBefore.setPictureURL(item.getPictureURL());
        itemBefore.setPrice(item.getPrice());
        itemBefore.setTotalQty(item.getTotalQty());
        itemBefore.setDescription(item.getDescription());
        
        itemRepository.save(itemBefore);
        return itemBefore;
    }
  
    public Page<Item> readAllItem(Pageable pageable) {
        return itemRepository.findAll(pageable);
    }
    
    public Item readItemByIdItem(Long id) {
        return itemRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Item not found"));
    }
  
    public Page<Item> readItemByAvailableGreaterThan(Integer min, Pageable pageable) {
        if (min < 0) {
            throw new InvalidValueException("Invalid value");
        }
        return itemRepository.findAllByAvailableQtyGreaterThan(min,pageable);
    }
  
    public ResponseEntity deleteItem(Long id) {
        Item item = itemRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Item not found"));
    
        if (item.getOwners().size() > 0) {
            throw new InvalidValueException("there's employee who still has " + item.getItemName());
        }
    
        item.setIsActive(false);
        itemRepository.save(item);
    
        return ResponseEntity.ok().build();
    }

}
