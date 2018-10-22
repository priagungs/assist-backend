package com.future.office_inventory_system.service;

import com.future.office_inventory_system.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface TransactionService {
  
    ResponseEntity createTransaction(Transaction transaction);
    ResponseEntity updateTransaction(Transaction transaction);
    ResponseEntity deleteTransaction(Long id);
    Transaction readTransactionByIdTransaction(Long id);
    Page<Transaction> readAllTransactions(Pageable pageable);

  
}
