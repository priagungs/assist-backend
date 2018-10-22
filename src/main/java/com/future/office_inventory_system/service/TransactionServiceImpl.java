package com.future.office_inventory_system.service;


import com.future.office_inventory_system.model.Transaction;
import com.future.office_inventory_system.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    TransactionRepository repository;

    @Autowired
    ItemTransactionService itemTransactionService;

    public Transaction createTransaction(Transaction transaction) {
        return null;
    }

    public Page<Transaction> readAllTransactions(Transaction transaction) {
        return null;
    }

    public Transaction readTransactionByIdTransaction(Long id) {
        return null;
    }

    public Transaction updateTransaction(Transaction transaction) {
        return null;
    }

    public ResponseEntity deleteTransaction(Long id) {
        return null;
    }
}
