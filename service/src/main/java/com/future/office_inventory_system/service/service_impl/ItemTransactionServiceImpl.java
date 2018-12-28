package com.future.office_inventory_system.service.service_impl;

import com.future.office_inventory_system.exception.InvalidValueException;
import com.future.office_inventory_system.exception.NotFoundException;
import com.future.office_inventory_system.model.entity_model.Item;
import com.future.office_inventory_system.model.entity_model.ItemTransaction;
import com.future.office_inventory_system.model.entity_model.Transaction;
import com.future.office_inventory_system.repository.ItemTransactionRepository;
import com.future.office_inventory_system.service.service_interface.ItemService;
import com.future.office_inventory_system.service.service_interface.ItemTransactionService;
import com.future.office_inventory_system.service.service_interface.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ItemTransactionServiceImpl implements ItemTransactionService {

    @Autowired
    private ItemTransactionRepository repository;

    @Autowired
    private ItemService itemService;

    @Autowired
    private TransactionService transactionService;

    @Transactional
    public ItemTransaction createItemTransaction(ItemTransaction itemTransaction) {
        if (itemTransaction.getBoughtQty() <= 0) {
            throw new InvalidValueException("Bought quantity must be greater than 0");
        }

        itemTransaction.setTransaction(transactionService
                .readTransactionByIdTransaction(itemTransaction.getTransaction().getIdTransaction()));

        Item item = itemService.readItemByIdItem(itemTransaction.getItem().getIdItem());
        item.setTotalQty(item.getTotalQty() + itemTransaction.getBoughtQty());
        item.setAvailableQty(item.getAvailableQty() + itemTransaction.getBoughtQty());
        itemService.updateItem(item);

        itemTransaction.setItem(item);

        return repository.save(itemTransaction);

    }

    @Transactional
    public ItemTransaction updateItemTransaction(ItemTransaction itemTransaction) {
        ItemTransaction before = repository
                .findById(itemTransaction.getIdItemTransaction())
                .orElseThrow(() -> new NotFoundException("Item Transaction not found"));
        if (itemTransaction.getBoughtQty() <= 0) {
            throw new InvalidValueException("Bought quantity must be greater than 0");
        }

        before.setBoughtQty(itemTransaction.getBoughtQty());
        before.setPrice(itemTransaction.getPrice());

        Item item = before.getItem();
        item.setTotalQty(item.getTotalQty() + itemTransaction.getBoughtQty());
        itemService.updateItem(item);
        itemTransaction.setItem(item);

        return repository.save(before);
    }

    public Page<ItemTransaction> readAllItemTransactions(Pageable pageable) {

        return repository.findAll(pageable);
    }

    public ItemTransaction readItemTransactionByIdItemTransaction(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Item Transaction not found"));
    }

    public Page<ItemTransaction> readAllItemTransactionsByTransaction(Transaction transaction, Pageable pageable) {
        Page<ItemTransaction> itemTransactionPage = repository.findAllByTransaction(transaction, pageable);
        if (itemTransactionPage.getTotalElements() == 0) {
            throw new NotFoundException("No transaction found");
        } else {
            return itemTransactionPage;
        }
    }

    @Transactional
    public ResponseEntity deleteItemTransaction(Long id) {
        ItemTransaction itemTransaction = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("ItemTransaction not found"));

        Item item = itemTransaction.getItem();

        if (item.getAvailableQty() < itemTransaction.getBoughtQty()) {
            throw new InvalidValueException("Insufficient item quantity");
        }

        item.setTotalQty(item.getTotalQty() - itemTransaction.getBoughtQty());
        item.setAvailableQty(item.getAvailableQty() - itemTransaction.getBoughtQty());
        itemService.updateItem(item);

        repository.delete(itemTransaction);
        return ResponseEntity.ok().build();
    }
}
