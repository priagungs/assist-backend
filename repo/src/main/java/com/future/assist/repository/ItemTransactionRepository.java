package com.future.assist.repository;

import com.future.assist.model.entity_model.ItemTransaction;
import com.future.assist.model.entity_model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ItemTransactionRepository extends JpaRepository<ItemTransaction, Long> {
    public Page<ItemTransaction> findAllByTransaction(Transaction transaction, Pageable pageable);
}

