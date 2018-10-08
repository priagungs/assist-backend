package com.future.OfficeInventorySystem.repository;

import com.future.OfficeInventorySystem.model.Item;
import com.future.OfficeInventorySystem.model.ItemTransaction;
import com.future.OfficeInventorySystem.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemTransactionRepository extends JpaRepository<ItemTransaction, Long> {

    ItemTransaction findByIdItemTransaction(Long idItemTransaction);
    List<ItemTransaction> findAllByTransaction(Transaction transaction);
    List<ItemTransaction> findAllByItem(Item item);

}
