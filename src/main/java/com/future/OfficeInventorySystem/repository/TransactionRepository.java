package com.future.OfficeInventorySystem.repository;

import com.future.OfficeInventorySystem.model.Transaction;
import com.future.OfficeInventorySystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Transaction findByIdTransaction(Long idTransaction);
    List<Transaction> findAllByAdmin(User admin);

}
