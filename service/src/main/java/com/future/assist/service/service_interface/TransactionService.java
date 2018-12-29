package com.future.assist.service.service_interface;

import com.future.assist.model.entity_model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface TransactionService {

    Integer MAX_ALLOWABLE_MILISECONDS_TO_UPDATE = 3600000;

    Transaction createTransaction(Transaction transaction);

    Page<Transaction> readAllTransactions(Pageable pageable);

    Transaction readTransactionByIdTransaction(Long id);

    ResponseEntity deleteTransaction(Long id);

}
