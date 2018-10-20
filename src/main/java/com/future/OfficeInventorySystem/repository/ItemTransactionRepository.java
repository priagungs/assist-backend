package com.future.OfficeInventorySystem.repository;

import com.future.OfficeInventorySystem.model.Item;
import com.future.OfficeInventorySystem.model.ItemTransaction;
import com.future.OfficeInventorySystem.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ItemTransactionRepository extends JpaRepository<ItemTransaction, Long> {

    Page<ItemTransaction> findAllByTransaction(Transaction transaction, Pageable pageable);
    
    Page<ItemTransaction> findAllByItem(Item item, Pageable pageable);

}

