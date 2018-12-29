package com.future.assist.mapper;

import com.future.assist.model.entity_model.ItemTransaction;
import com.future.assist.model.entity_model.Transaction;
import com.future.assist.model.request_model.transaction.ItemTransactionRequest;
import com.future.assist.model.request_model.transaction.TransactionCreateRequest;
import com.future.assist.model.response_model.ItemTransactionResponse;
import com.future.assist.model.response_model.PageResponse;
import com.future.assist.model.response_model.TransactionResponse;
import com.future.assist.service.service_interface.ItemService;
import com.future.assist.service.service_interface.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TransactionMapper {

    @Autowired
    UserMapper userMapper;

    @Autowired
    ItemMapper itemMapper;

    @Autowired
    ItemService itemService;

    @Autowired
    UserService userService;

    public ItemTransactionResponse itemTransactionEntityToResponse(ItemTransaction itemTransaction) {
        ItemTransactionResponse response = new ItemTransactionResponse();
        response.setBoughtQty(itemTransaction.getBoughtQty());
        response.setIdItemTransaction(itemTransaction.getIdItemTransaction());
        response.setPrice(itemTransaction.getPrice());
        response.setItem(itemMapper.entityToResponse(itemTransaction.getItem()));
        return response;
    }

    public ItemTransaction itemTransactionRequestToEntity(ItemTransactionRequest itemTransactionRequest) {
        ItemTransaction itemTransaction = new ItemTransaction();
        itemTransaction.setBoughtQty(itemTransactionRequest.getBoughtQty());
        itemTransaction.setPrice(itemTransactionRequest.getPrice());
        itemTransaction.setItem(itemService.readItemByIdItem(itemTransactionRequest.getItem().getIdItem()));
        return itemTransaction;
    }

    public Transaction transactionRequestToEntity(TransactionCreateRequest transactionCreateRequest) {
        Transaction transaction = new Transaction();
        transaction.setSupplier(transactionCreateRequest.getSupplier());
        transaction.setAdmin(userService.readUserByIdUser(transactionCreateRequest.getAdmin().getIdUser()));
        List<ItemTransaction> itemTransactions = new ArrayList<>();
        for (ItemTransactionRequest itemTransactionRequest : transactionCreateRequest.getItemTransactions()) {
            itemTransactions.add(itemTransactionRequestToEntity(itemTransactionRequest));
        }
        transaction.setItemTransactions(itemTransactions);
        return transaction;
    }

    public TransactionResponse transactionEntityToResponse(Transaction transaction) {
        TransactionResponse response = new TransactionResponse();
        response.setAdmin(userMapper.entityToResponse(transaction.getAdmin()));
        response.setIdTransaction(transaction.getIdTransaction());
        List<ItemTransactionResponse> itemTransactionResponses = new ArrayList<>();
        for (ItemTransaction itemTransaction : transaction.getItemTransactions()) {
            itemTransactionResponses.add(itemTransactionEntityToResponse(itemTransaction));
        }
        response.setItemTransactions(itemTransactionResponses);
        response.setSupplier(transaction.getSupplier());
        response.setTransactionDate(transaction.getTransactionDate());
        return response;
    }

    public PageResponse<TransactionResponse> pageToPageResponse(Page<Transaction> transactions) {
        PageResponse<TransactionResponse> response = new PageResponse<>();
        List<TransactionResponse> content = new ArrayList<>();
        for (Transaction transaction : transactions.getContent()) {
            content.add(transactionEntityToResponse(transaction));
        }
        response.setContent(content);
        response.setTotalElements(transactions.getTotalElements());
        response.setFirst(transactions.isFirst());
        response.setLast(transactions.isLast());
        response.setNumberOfElements(transactions.getNumberOfElements());
        response.setTotalPages(transactions.getTotalPages());
        response.setSize(transactions.getSize());
        response.setNumber(transactions.getNumber());
        return response;
    }

}
