package com.future.assist.repository;

import com.future.assist.model.entity_model.User;
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

    Optional<User> findByUsernameAndIsActive(String username, Boolean isActive);

    Page<User> findAllBySuperiorAndIsActive(User superior, Boolean isActive, Pageable page);

    Optional<User> findByIdUserAndIsActive(Long idUser, Boolean isActive);

    Page<User> findAllByIsActive(Boolean isActive, Pageable pageable);

    Page<User> findByNameIgnoreCaseContainingAndIsActive(String keyword, Boolean isActive, Pageable pageable);

}
