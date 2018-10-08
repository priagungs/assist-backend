package com.future.OfficeInventorySystem.repository;

import com.future.OfficeInventorySystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u where u.idUser = idUser")
    User findByIdUser(Long idUser);

    @Query("select u from User u where u.username = username")
    User findByUsername(String username);

    @Query("select u from User u where u.name = name")
    List<User> findByName(String name);

    @Query("select u from User u where u.role = role")
    List<User> findByRole(String role);

    @Query("select u from User u where u.division = division")
    List<User> findByDivision(String division);

    @Query("select u from User u where u.superior.idUser = superior.idUser")
    List<User> findBySuperior(User superior);

    @Query("select u from User u where u.isAdmin = True")
    List<User> findAdmins();

    @Query("select u from User u")
    List<User> findAll();
}
