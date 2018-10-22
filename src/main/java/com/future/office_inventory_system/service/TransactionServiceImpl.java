package com.future.office_inventory_system.service;


import com.future.office_inventory_system.exception.NotFoundException;
import com.future.office_inventory_system.model.Transaction;
import com.future.office_inventory_system.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ItemTransactionService itemTransactionService;

    @Autowired
    private UserService userService;

    public Transaction createTransaction(Transaction transaction) {

        if(userService.readUserByIdUser(transaction.getAdmin().getIdUser()) == null){
            throw new NotFoundException("not found transaction admin");
        }

        transactionRepository.save(transaction);
        return transaction;
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
