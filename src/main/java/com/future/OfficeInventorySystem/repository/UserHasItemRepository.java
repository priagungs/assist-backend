package com.future.OfficeInventorySystem.repository;

import com.future.OfficeInventorySystem.model.Item;
import com.future.OfficeInventorySystem.model.User;
import com.future.OfficeInventorySystem.model.UserHasItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserHasItemRepository extends JpaRepository<UserHasItem, Long> {

    UserHasItem findByIdUserHasItem(Long idUserHasItem);
    List<UserHasItem> findAllByUser(User user);
    List<UserHasItem> findAllByItem(Item item);

}
