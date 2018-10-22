package com.future.office_inventory_system.service;

import com.future.office_inventory_system.model.ItemTransaction;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface ItemTransactionService {
    ItemTransaction createItemTransaction(ItemTransaction itemTransaction);
    ResponseEntity deleteItemTransaction(Long id);
}
