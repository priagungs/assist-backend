package com.future.OfficeInventorySystem.repository;

import com.future.OfficeInventorySystem.model.Transaction;
import com.future.OfficeInventorySystem.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Page<Transaction> findAllByAdmin(User admin, Pageable pageable);

}
