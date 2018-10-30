package com.future.office_inventory_system.controller;

import com.future.office_inventory_system.exception.UnauthorizedException;
import com.future.office_inventory_system.model.Transaction;
import com.future.office_inventory_system.service.TransactionService;
import com.future.office_inventory_system.value_object.LoggedinUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @Autowired
    LoggedinUserInfo loggedinUserInfo;

    @PostMapping("/transactions")
    Transaction createTransactions(@RequestBody Transaction transaction) {
        if (!loggedinUserInfo.getUser().getIsAdmin() &&
                transaction.getAdmin().getIdUser() != loggedinUserInfo.getUser().getIdUser()) {
            throw new UnauthorizedException("you are not permitted to create transaction");
        }
        return transactionService.createTransaction(transaction);
    }

    @GetMapping("/api/transactions")
    Page<Transaction> readAllTransactions(@RequestParam("page") Integer page, @RequestParam("limit") Integer limit) {
        if (!loggedinUserInfo.getUser().getIsAdmin()) {
            throw new UnauthorizedException("you are not permitted to read transaction");
        }
        return transactionService.readAllTransactions(PageRequest.of(page, limit));
    }

    @GetMapping("/api/transactions/{id}")
    Transaction readTransactionById(@PathVariable("id") Long id) {
        if (!loggedinUserInfo.getUser().getIsAdmin()) {
            throw new UnauthorizedException("you are not permitted to read transaction");
        }
        return transactionService.readTransactionByIdTransaction(id);
    }

    @DeleteMapping("/api/transactions")
    ResponseEntity deleteTransaction(@RequestParam Transaction transaction) {
        if (!loggedinUserInfo.getUser().getIsAdmin()) {
            throw new UnauthorizedException("you are not permitted to read transaction");
        }
        return transactionService.deleteTransaction(transaction.getIdTransaction());
    }


}
