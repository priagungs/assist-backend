package com.future.OfficeInventorySystem.repository;

import com.future.OfficeInventorySystem.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
}
