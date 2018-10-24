package com.future.office_inventory_system.service;

import com.future.office_inventory_system.exception.ConflictException;
import com.future.office_inventory_system.exception.InvalidValueException;
import com.future.office_inventory_system.exception.NotFoundException;
import com.future.office_inventory_system.model.Item;
import com.future.office_inventory_system.model.ItemTransaction;
import com.future.office_inventory_system.model.Transaction;
import com.future.office_inventory_system.repository.ItemTransactionRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Data
public class ItemTransactionServiceImpl {
    
    @Autowired
    private ItemTransactionRepository repository;
    
    @Autowired
    private ItemService itemService;
    
    @Autowired
    private TransactionService transactionService;
    
    public ItemTransaction createItemTransaction(ItemTransaction itemTransaction) {
       if (repository.findById(itemTransaction.getIdItemTransaction()).isPresent()) {
           throw new ConflictException(itemTransaction.getTransaction().toString() + " is already exist");
       }
       
       itemTransaction.setTransaction(transactionService
           .readTransactionByIdTransaction(itemTransaction.getTransaction().getIdTransaction()));
       
       itemTransaction.setItem(itemService
           .readItemByIdItem(itemTransaction.getItem().getIdItem()));
       
       Item item = itemTransaction.getItem();
       item.setTotalQty(item.getTotalQty() + itemTransaction.getBoughtQty());
       
       itemService.updateItem(item);
       itemTransaction.setItem(item);
    
       return repository.save(itemTransaction);
       
    }
    
    public ItemTransaction updateItemTransaction(ItemTransaction itemTransaction) {
        ItemTransaction before = repository
            .findById(itemTransaction.getIdItemTransaction())
            .orElseThrow(() -> new NotFoundException("Item Transaction not found"));
        before.setTransaction(transactionService
            .readTransactionByIdTransaction(itemTransaction.getTransaction().getIdTransaction()));
        before.setItem(itemService
            .readItemByIdItem(itemTransaction.getItem().getIdItem()));
        
        before.setBoughtQty(itemTransaction.getBoughtQty());
        before.setPrice(itemTransaction.getPrice());
        
        Item item = before.getItem();
        item.setTotalQty(item.getTotalQty() + itemTransaction.getBoughtQty());
        itemService.updateItem(item);
        itemTransaction.setItem(item);
        
        return repository.save(before);
    }
    
    public Page<ItemTransaction> readAllItemTransactions(Pageable pageable) {
        return repository.findAll(pageable);
    }
    
    public ItemTransaction readItemTransactionByIdItemTransaction(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new NotFoundException("Item Transaction not found"));
    }
    
    public Page<ItemTransaction> readAllItemTransactionsByTransaction(Transaction transaction, Pageable pageable) {
        return repository.findAllByTransaction(transaction, pageable);
    }
    
    public ResponseEntity deleteItemTransaction(Long id) {
        ItemTransaction itemTransaction = repository.findById(id)
            .orElseThrow(() -> new NotFoundException("ItemTransaction not found"));
        
        Item item = itemTransaction.getItem();
        
        if (item.getAvailableQty() < itemTransaction.getBoughtQty()) {
            throw new InvalidValueException("Insufficient item quantity");
        }
    
        item.setTotalQty(item.getTotalQty() - itemTransaction.getBoughtQty());
        itemService.updateItem(item);
        
        repository.delete(itemTransaction);
        return ResponseEntity.ok().build();
        
    }
}
