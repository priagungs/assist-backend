package com.future.office_inventory_system.repository;

import com.future.office_inventory_system.model.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    Optional<Item> findByItemNameAndIsActive(String itemName, Boolean active);
    Page<Item> findAllByAvailableQtyGreaterThanAndIsActive(Integer min, Boolean active, Pageable pageable);
    Page<Item> findAllByIsActive(Boolean active, Pageable pageable);
    Optional<Item> findByIdItemAndIsActive(Long id, Boolean active);
    List<Item> findAllByIsActive(Boolean active);

}
