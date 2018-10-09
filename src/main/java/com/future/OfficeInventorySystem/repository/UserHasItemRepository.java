package com.future.OfficeInventorySystem.repository;

import com.future.OfficeInventorySystem.model.Item;
import com.future.OfficeInventorySystem.model.User;
import com.future.OfficeInventorySystem.model.UserHasItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface UserHasItemRepository extends JpaRepository<UserHasItem, Long> {

    UserHasItem findByIdUserHasItem(Long idUserHasItem);
    Set<UserHasItem> findAllByUser(User user);
    Set<UserHasItem> findAllByItem(Item item);

}
