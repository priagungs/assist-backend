package com.future.assist.repository;

import com.future.assist.model.entity_model.Item;
import com.future.assist.model.entity_model.User;
import com.future.assist.model.entity_model.UserHasItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserHasItemRepository extends JpaRepository<UserHasItem, Long> {

    Page<UserHasItem> findAllByUser(User user, Pageable pageable);

    Page<UserHasItem> findAllByItem(Item item, Pageable pageable);

    List<UserHasItem> findAllByUserAndItem(User user, Item item);


}
