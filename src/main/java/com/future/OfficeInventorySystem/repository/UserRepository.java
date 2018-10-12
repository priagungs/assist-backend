package com.future.OfficeInventorySystem.repository;

import com.future.OfficeInventorySystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByIdUser(Long idUser);
    User findByUsername(String username);
    List<User> findAllByName(String name);
    List<User> findAllByRole(String role);
    List<User> findAllByDivision(String division);
    List<User> findAllBySuperior(User superior);
    List<User> findAllByIsAdmin(Boolean isAdmin);
}
