package com.future.assist.repository;

import com.future.assist.model.entity_model.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    Optional<Item> findByItemNameIgnoreCaseAndIsActive(String itemName, Boolean active);

    Page<Item> findAllByAvailableQtyGreaterThanAndIsActive(Integer min, Boolean active, Pageable pageable);

    Page<Item> findAllByIsActive(Boolean active, Pageable pageable);

    Optional<Item> findByIdItemAndIsActive(Long id, Boolean active);

    Page<Item> findByItemNameIgnoreCaseContainingAndIsActive(String name, Boolean active, Pageable pageable);

    Page<Item> findByItemNameIgnoreCaseContainingAndAvailableQtyGreaterThanAndIsActive(
            String keyword, Integer min, Boolean active, Pageable pageable);
}
