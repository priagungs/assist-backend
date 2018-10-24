package com.future.office_inventory_system.service;

import com.future.office_inventory_system.model.ItemTransaction;
<<<<<<< HEAD
import com.future.office_inventory_system.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
=======
>>>>>>> 2d80886362df7d363cc80ae4fcc549bc4ed6208e
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface ItemTransactionService {
<<<<<<< HEAD
    
    ItemTransaction createItemTransaction(ItemTransaction itemTransaction);
    
    ItemTransaction updateItemTransaction(ItemTransaction itemTransaction);
    
    Page<ItemTransaction> readAllItemTransactions(Pageable pageable);
    
    ItemTransaction readItemTransactionByIdItemTransaction(Long id);
    
    Page<ItemTransaction> readAllItemTransactionsByTransaction(Transaction transaction, Pageable pageable);
    
    ResponseEntity deleteItemTransaction(Long id);
    
    
    
=======
    ItemTransaction createItemTransaction(ItemTransaction itemTransaction);
    ResponseEntity deleteItemTransaction(Long id);
>>>>>>> 2d80886362df7d363cc80ae4fcc549bc4ed6208e
}
