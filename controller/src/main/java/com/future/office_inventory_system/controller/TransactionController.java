package com.future.office_inventory_system.controller;

import com.future.office_inventory_system.exception.UnauthorizedException;
import com.future.office_inventory_system.model.Transaction;
import com.future.office_inventory_system.printer.PrinterService;
import com.future.office_inventory_system.service.TransactionService;
import com.future.office_inventory_system.value_object.LoggedinUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @Autowired
    LoggedinUserInfo loggedinUserInfo;
    
    @Autowired
    PrinterService p;

    @PostMapping("/transactions")
    Transaction createTransactions(@RequestBody Transaction transaction) {
        if (!loggedinUserInfo.getUser().getIsAdmin() &&
                transaction.getAdmin().getIdUser() != loggedinUserInfo.getUser().getIdUser()) {
            throw new UnauthorizedException("you are not permitted to create transaction");
        }
        Transaction createdTransaction = transactionService.createTransaction(transaction);
        p.printInvoice(transactionService.readTransactionByIdTransaction(createdTransaction.getIdTransaction()));
        return createdTransaction;
        
    }

    @GetMapping("/transactions")
    Page<Transaction> readAllTransactions(@RequestParam("page") Integer page,
                                          @RequestParam("limit") Integer limit,
                                          @RequestParam("sort") String sort) {
        if (!loggedinUserInfo.getUser().getIsAdmin()) {
            throw new UnauthorizedException("you are not permitted to read transaction");
        }
        if (sort.equals("transactionDate")) {
            return transactionService.readAllTransactions(PageRequest.of(page, limit, Sort.Direction.DESC, sort));
        }
        else {
            return transactionService.readAllTransactions(PageRequest.of(page, limit, Sort.Direction.ASC, sort));
        }
    }

    @GetMapping("/transactions/{id}")
    Transaction readTransactionById(@PathVariable("id") Long id) {
        if (!loggedinUserInfo.getUser().getIsAdmin()) {
            throw new UnauthorizedException("you are not permitted to read transaction");
        }
        return transactionService.readTransactionByIdTransaction(id);
    }

    @DeleteMapping("/transactions")
    ResponseEntity deleteTransaction(@RequestBody Transaction transaction) {
        if (!loggedinUserInfo.getUser().getIsAdmin()) {
            throw new UnauthorizedException("you are not permitted to read transaction");
        }
        return transactionService.deleteTransaction(transaction.getIdTransaction());
    }


}
