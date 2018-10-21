package com.future.office_inventory_system.repository;

import com.future.office_inventory_system.model.Item;
import com.future.office_inventory_system.model.ItemTransaction;
import com.future.office_inventory_system.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ItemTransactionRepository extends JpaRepository<ItemTransaction, Long> {

    Page<ItemTransaction> findAllByTransaction(Transaction transaction, Pageable pageable);
    
    Page<ItemTransaction> findAllByItem(Item item, Pageable pageable);

}

