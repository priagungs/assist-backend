package com.future.office_inventory_system.service;

import com.future.office_inventory_system.exception.ConflictException;
import com.future.office_inventory_system.exception.InvalidValueException;
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

    @Autowired
    private UserHasItemService userHasItemService;
    
    public Item createItem(Item item) {
        if (itemRepository.findByItemNameAndIsActive(item.getItemName(), true).isPresent()) {
            throw new ConflictException(item.getItemName() + " is exist");
        }
        
        if (item.getTotalQty() < 0 ||  item.getPrice() < 0) {
            throw new InvalidValueException("Invalid value");
        }
        
        item.setAvailableQty(item.getTotalQty());
        return itemRepository.save(item);
    }
    
    public Item updateItem(Item item) {
        Item itemBefore = itemRepository
            .findByIdItemAndIsActive(item.getIdItem(), true)
            .orElseThrow(() -> new NotFoundException("Item not found"));

        if (item.getTotalQty() < itemBefore.getTotalQty() - itemBefore.getAvailableQty() || item.getPrice() < 0 ||
                item.getAvailableQty() < 0) {
            throw new InvalidValueException("Invalid value");
        }
        
        itemBefore.setItemName(item.getItemName());
        itemBefore.setPictureURL(item.getPictureURL());
        itemBefore.setPrice(item.getPrice());
        itemBefore.setDescription(item.getDescription());
        if (item.getAvailableQty() == null) {
            itemBefore.setAvailableQty(itemBefore.getAvailableQty() + item.getTotalQty() - itemBefore.getTotalQty());
        }
        else {
            itemBefore.setAvailableQty(item.getAvailableQty());
        }
        itemBefore.setTotalQty(item.getTotalQty());

        return itemRepository.save(itemBefore);
    }
  
    public Page<Item> readAllItems(Pageable pageable) {
        
        return itemRepository.findAllByIsActive(true, pageable);
    }
    
    public Item readItemByIdItem(Long id) {
        return itemRepository.findByIdItemAndIsActive(id, true)
            .orElseThrow(() -> new NotFoundException("Item not found"));
    }
  
    public Page<Item> readItemsByAvailableGreaterThan(Integer min, Pageable pageable) {
        if (min < 0) {
            throw new InvalidValueException("Invalid value");
        }
        return itemRepository.findAllByAvailableQtyGreaterThanAndIsActive(min, true, pageable);
    }
  
    public ResponseEntity deleteItem(Long id) {
        Item item = itemRepository.findByIdItemAndIsActive(id, true)
            .orElseThrow(() -> new NotFoundException("Item not found"));
    
        if (item.getOwners().size() > 0) {
            throw new InvalidValueException("there's employee who still has " + item.getItemName());
        }
    
        item.setIsActive(false);
        itemRepository.save(item);
    
        return ResponseEntity.ok().build();
    }

}
