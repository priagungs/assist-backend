package com.future.office_inventory_system.repository;

import com.future.office_inventory_system.model.Item;
import com.future.office_inventory_system.model.User;
import com.future.office_inventory_system.model.UserHasItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserHasItemRepository extends JpaRepository<UserHasItem, Long> {

    Page<UserHasItem> findAllByUser(User user, Pageable pageable);
    
    Page<UserHasItem> findAllByItem(Item item, Pageable pageable);

}
