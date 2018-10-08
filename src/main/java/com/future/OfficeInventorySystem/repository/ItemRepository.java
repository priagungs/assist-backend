package com.future.OfficeInventorySystem.repository;

import com.future.OfficeInventorySystem.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("select i from Item i where i.idItem = idItem")
    Item findByIdItem(Long idItem);

    @Query("select i from Item i where i.itemName = itemName")
    List<Item> findByItemName(String itemName);

    @Query("select i from Item i where i.availableQty > 0")
    List<Item> findAllByAvailableQty();


}
