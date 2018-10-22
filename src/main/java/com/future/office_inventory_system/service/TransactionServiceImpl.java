package com.future.office_inventory_system.service;

import com.future.office_inventory_system.exception.InvalidValueException;
import com.future.office_inventory_system.exception.NotFoundException;
import com.future.office_inventory_system.model.ItemTransaction;
import com.future.office_inventory_system.model.Transaction;
import com.future.office_inventory_system.model.User;
import com.future.office_inventory_system.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    TransactionRepository repository;

    @Autowired
    ItemTransactionService itemTransactionService;

    @Autowired
    UserService userService;

    public Transaction createTransaction(Transaction transaction) {
        User admin = userService.readUserByIdUser(transaction.getAdmin().getIdUser());
        if (!admin.getIsAdmin()) {
            throw new InvalidValueException("admin is not an admin");
        }
        transaction.setAdmin(admin);
        transaction.setTransactionDate(new Date());
        List<ItemTransaction> itemTransactions = new ArrayList<>();
        for (ItemTransaction el : transaction.getItemTransactions()) {
            ItemTransaction itemTrx = itemTransactionService.createItemTransaction(el);
            itemTrx.setTransaction(transaction);
            itemTransactions.add(itemTrx);
        }
        transaction.setItemTransactions(itemTransactions);
        return repository.save(transaction);

    }

    public Page<Transaction> readAllTransactions(Transaction transaction, Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Transaction readTransactionByIdTransaction(Long id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("transaction not found"));
    }

    public Transaction updateTransaction(Transaction transaction) {
        return null;
    }

    public ResponseEntity deleteTransaction(Long id) {
        return null;
    }
}
