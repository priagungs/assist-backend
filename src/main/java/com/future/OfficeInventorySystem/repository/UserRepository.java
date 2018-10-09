package com.future.OfficeInventorySystem.repository;

import com.future.OfficeInventorySystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByIdUser(Long idUser);
    User findByUsername(String username);
    Set<User> findAllByName(String name);
    Set<User> findAllByRole(String role);
    Set<User> findAllByDivision(String division);
    Set<User> findAllBySuperior(User superior);
    Set<User> findAllByIsAdmin(Boolean isAdmin);
}
