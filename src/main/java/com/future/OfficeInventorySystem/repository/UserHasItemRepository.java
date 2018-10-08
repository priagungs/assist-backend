package com.future.OfficeInventorySystem.repository;

import com.future.OfficeInventorySystem.model.UserHasItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserHasItemRepository extends JpaRepository<UserHasItem, Long> {
}
