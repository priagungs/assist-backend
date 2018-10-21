package com.future.office_inventory_system.repository;

import com.future.office_inventory_system.model.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    Optional<Item> findByItemName(String itemName);
    Page<Item> findAllByAvailableQtyGreaterThan(Integer min, Pageable pageable);

}
