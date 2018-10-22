package com.future.office_inventory_system.service;

import com.future.office_inventory_system.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface TransactionService {

    Transaction createTransaction(Transaction transaction);
    Page<Transaction> readAllTransactions(Transaction transaction);
    Transaction readTransactionByIdTransaction(Long id);
    Transaction updateTransaction(Transaction transaction);
    ResponseEntity deleteTransaction(Long id);

}
