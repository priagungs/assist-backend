package com.future.OfficeInventorySystem.repository;

import com.future.OfficeInventorySystem.model.Item;
import com.future.OfficeInventorySystem.model.User;
import com.future.OfficeInventorySystem.model.UserHasItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface UserHasItemRepository extends JpaRepository<UserHasItem, Long> {

    Page<UserHasItem> findAllByUser(User user, Pageable pageable);
    Page<UserHasItem> findAllByItem(Item item, Pageable pageable);

}
