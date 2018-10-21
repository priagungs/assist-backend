package com.future.office_inventory_system.repository;

import com.future.office_inventory_system.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;



@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    
    Page<User> findAllBySuperior(User superior, Pageable page);
    
    Page<User> findAllByIsAdmin(Boolean isAdmin, Pageable page);

}
