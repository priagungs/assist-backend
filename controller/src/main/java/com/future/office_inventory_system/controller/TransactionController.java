package com.future.office_inventory_system.controller;

import com.future.office_inventory_system.exception.UnauthorizedException;
import com.future.office_inventory_system.model.entity_model.ItemTransaction;
import com.future.office_inventory_system.model.entity_model.Transaction;
import com.future.office_inventory_system.model.request_model.transaction.ItemTransactionRequest;
import com.future.office_inventory_system.model.request_model.transaction.TransactionCreateRequest;
import com.future.office_inventory_system.model.request_model.transaction.TransactionModelRequest;
import com.future.office_inventory_system.printer.PrinterService;
import com.future.office_inventory_system.service.service_impl.LoggedinUserInfo;
import com.future.office_inventory_system.service.service_interface.ItemService;
import com.future.office_inventory_system.service.service_interface.TransactionService;
import com.future.office_inventory_system.service.service_interface.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @Autowired
    UserService userService;

    @Autowired
    ItemService itemService;

    @Autowired
    LoggedinUserInfo loggedinUserInfo;

    @Autowired
    PrinterService printerService;

    @PostMapping("/transactions")
    Transaction createTransactions(@RequestBody TransactionCreateRequest transactionCreateRequest) {
        if (!loggedinUserInfo.getUser().getIsAdmin() &&
                transactionCreateRequest.getAdmin().getIdUser() != loggedinUserInfo.getUser().getIdUser()) {
            throw new UnauthorizedException("you are not permitted to create transaction");
        }
        Transaction transaction = new Transaction();
        transaction.setSupplier(transactionCreateRequest.getSupplier());
        transaction.setAdmin(userService.readUserByIdUser(transactionCreateRequest.getAdmin().getIdUser()));
        List<ItemTransaction> itemTransactions = new ArrayList<>();
        for (ItemTransactionRequest itemTransactionRequest : transactionCreateRequest.getItemTransactions()) {
            ItemTransaction itemTransaction = new ItemTransaction();
            itemTransaction.setBoughtQty(itemTransactionRequest.getBoughtQty());
            itemTransaction.setPrice(itemTransactionRequest.getPrice());
            itemTransaction.setItem(itemService.readItemByIdItem(itemTransactionRequest.getItem().getIdItem()));
            itemTransactions.add(itemTransaction);
        }
        transaction.setItemTransactions(itemTransactions);

        Transaction createdTransaction = transactionService.createTransaction(transaction);
        printerService.printInvoice(transactionService.readTransactionByIdTransaction(createdTransaction.getIdTransaction()));
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
        } else {
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
    ResponseEntity deleteTransaction(@RequestBody TransactionModelRequest transaction) {
        if (!loggedinUserInfo.getUser().getIsAdmin()) {
            throw new UnauthorizedException("you are not permitted to read transaction");
        }
        return transactionService.deleteTransaction(transaction.getIdTransaction());
    }


}
