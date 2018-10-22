package com.future.office_inventory_system.service;

import com.future.office_inventory_system.model.ItemTransaction;
import com.future.office_inventory_system.model.Transaction;
import com.future.office_inventory_system.model.User;
import com.future.office_inventory_system.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImpl implements TransactionService {
  
    @Autowired
    private TransactionRepository transactionRepository;
    
    @Autowired
    private ItemTransactionService itemTransactionService;
    
    @Autowired
    private User admin;
    
    public Transaction createTransaction(Transaction transaction) {
    
        for (ItemTransaction itemTransaction : transaction.getItemTransactions()) {
            itemTransactionService.createItemTransaction(itemTransaction);
        }
        
        transactionRepository.save(transaction);
        return transaction;
    }
    public ResponseEntity updateTransaction(Transaction transaction) {}
    public ResponseEntity deleteTransaction(Long id) {}
    public Transaction readTransactionByIdTransaction(Long id) {}
    public Page<Transaction> readAllTransactions(Pageable pageable) {}

}
