package com.future.office_inventory_system.controller;

import com.future.office_inventory_system.exception.UnauthorizedException;
import com.future.office_inventory_system.mapper.TransactionMapper;
import com.future.office_inventory_system.model.entity_model.ItemTransaction;
import com.future.office_inventory_system.model.entity_model.Transaction;
import com.future.office_inventory_system.model.request_model.transaction.ItemTransactionRequest;
import com.future.office_inventory_system.model.request_model.transaction.TransactionCreateRequest;
import com.future.office_inventory_system.model.request_model.transaction.TransactionModelRequest;
import com.future.office_inventory_system.model.response_model.PageResponse;
import com.future.office_inventory_system.model.response_model.TransactionResponse;
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

    @Autowired
    TransactionMapper transactionMapper;

    @PostMapping("/transactions")
    TransactionResponse createTransactions(@RequestBody TransactionCreateRequest transactionCreateRequest) {
        if (!loggedinUserInfo.getUser().getIsAdmin() &&
                transactionCreateRequest.getAdmin().getIdUser() != loggedinUserInfo.getUser().getIdUser()) {
            throw new UnauthorizedException("you are not permitted to create transaction");
        }


        Transaction createdTransaction = transactionService.createTransaction(transactionMapper
                .transactionRequestToEntity(transactionCreateRequest));
        printerService.printInvoice(transactionService.readTransactionByIdTransaction(createdTransaction.getIdTransaction()));
        return transactionMapper.transactionEntityToResponse(createdTransaction);

    }

    @GetMapping("/transactions")
    PageResponse<TransactionResponse> readAllTransactions(@RequestParam("page") Integer page,
                                                  @RequestParam("limit") Integer limit,
                                                  @RequestParam("sort") String sort) {
        if (!loggedinUserInfo.getUser().getIsAdmin()) {
            throw new UnauthorizedException("you are not permitted to read transaction");
        }
        if (sort.equals("transactionDate")) {
            return transactionMapper.pageToPageResponse(transactionService
                    .readAllTransactions(PageRequest.of(page, limit, Sort.Direction.DESC, sort)));
        } else {
            return transactionMapper.pageToPageResponse(transactionService
                    .readAllTransactions(PageRequest.of(page, limit, Sort.Direction.ASC, sort)));
        }
    }

    @GetMapping("/transactions/{id}")
    TransactionResponse readTransactionById(@PathVariable("id") Long id) {
        if (!loggedinUserInfo.getUser().getIsAdmin()) {
            throw new UnauthorizedException("you are not permitted to read transaction");
        }
        return transactionMapper.transactionEntityToResponse(
                transactionService.readTransactionByIdTransaction(id));
    }

    @DeleteMapping("/transactions")
    ResponseEntity deleteTransaction(@RequestBody TransactionModelRequest transaction) {
        if (!loggedinUserInfo.getUser().getIsAdmin()) {
            throw new UnauthorizedException("you are not permitted to read transaction");
        }
        return transactionService.deleteTransaction(transaction.getIdTransaction());
    }


}
