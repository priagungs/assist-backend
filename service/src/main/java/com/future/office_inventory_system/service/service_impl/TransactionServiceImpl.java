package com.future.office_inventory_system.service.service_impl;

import com.future.office_inventory_system.exception.ForbiddenException;
import com.future.office_inventory_system.exception.InvalidValueException;
import com.future.office_inventory_system.exception.NotFoundException;
import com.future.office_inventory_system.model.entity_model.ItemTransaction;
import com.future.office_inventory_system.model.entity_model.Transaction;
import com.future.office_inventory_system.model.entity_model.User;
import com.future.office_inventory_system.repository.TransactionRepository;
import com.future.office_inventory_system.service.service_interface.ItemTransactionService;
import com.future.office_inventory_system.service.service_interface.TransactionService;
import com.future.office_inventory_system.service.service_interface.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    UserService userService;

    @Autowired
    private TransactionRepository repository;

    @Autowired
    private ItemTransactionService itemTransactionService;

    @Transactional
    public Transaction createTransaction(Transaction transaction) {

        User admin = userService.readUserByIdUser(transaction.getAdmin().getIdUser());
        if (!admin.getIsAdmin()) {
            throw new InvalidValueException("admin is not an admin");
        }
        List<ItemTransaction> itemTransactions = new ArrayList<>();
        Transaction newTrx = new Transaction();
        newTrx.setAdmin(admin);
        newTrx.setSupplier(transaction.getSupplier());
        newTrx.setTransactionDate(new Date());
        newTrx = repository.save(newTrx);
        for (ItemTransaction el : transaction.getItemTransactions()) {
            el.setTransaction(newTrx);
            ItemTransaction itemTrx = itemTransactionService.createItemTransaction(el);
            itemTransactions.add(itemTrx);
        }
        newTrx.setItemTransactions(itemTransactions);
        return repository.save(newTrx);
    }

    public Page<Transaction> readAllTransactions(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Transaction readTransactionByIdTransaction(Long id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("transaction not found"));
    }

    @Transactional
    public ResponseEntity deleteTransaction(Long id) {
        Transaction transaction = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("transaction not found"));
        if (new Date().getTime() - transaction.getTransactionDate().getTime() >
                TransactionService.MAX_ALLOWABLE_MILISECONDS_TO_UPDATE) {
            throw new ForbiddenException("transaction has been created for more than" +
                    TransactionService.MAX_ALLOWABLE_MILISECONDS_TO_UPDATE.toString() + "ms");
        }

        for (ItemTransaction el : transaction.getItemTransactions()) {
            itemTransactionService.deleteItemTransaction(el.getIdItemTransaction());
        }

        repository.delete(transaction);
        return ResponseEntity.ok().build();
    }
}
