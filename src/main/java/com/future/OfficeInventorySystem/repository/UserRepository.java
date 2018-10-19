package com.future.OfficeInventorySystem.repository;

import com.future.OfficeInventorySystem.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import org.springframework.stereotype.Repository;



@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    
    Page<User> findAllByName(String name, Pageable page);
    
    Page<User> findAllBySuperior(User superior, Pageable page);
    
    Page<User> findAllByIsAdmin(Boolean isAdmin, Pageable page);

}
