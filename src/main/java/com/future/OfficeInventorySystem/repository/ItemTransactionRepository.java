package com.future.OfficeInventorySystem.repository;

import com.future.OfficeInventorySystem.model.ItemTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemTransactionRepository extends JpaRepository<ItemTransaction, Long> {

}
