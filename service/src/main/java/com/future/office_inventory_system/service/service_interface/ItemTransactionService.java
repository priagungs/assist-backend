package com.future.office_inventory_system.service.service_interface;

import com.future.office_inventory_system.model.entity_model.ItemTransaction;
import com.future.office_inventory_system.model.entity_model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface ItemTransactionService {

    ItemTransaction createItemTransaction(ItemTransaction itemTransaction);

    ItemTransaction updateItemTransaction(ItemTransaction itemTransaction);

    Page<ItemTransaction> readAllItemTransactions(Pageable pageable);

    ItemTransaction readItemTransactionByIdItemTransaction(Long id);

    Page<ItemTransaction> readAllItemTransactionsByTransaction(Transaction transaction, Pageable pageable);

    ResponseEntity deleteItemTransaction(Long id);


}
