package com.future.assist.repository;

import com.future.assist.model.entity_model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    public Optional<User> findByUsernameAndIsActive(String username, Boolean isActive);

    public Page<User> findAllBySuperiorAndIsActive(User superior, Boolean isActive, Pageable page);

    public Optional<User> findByIdUserAndIsActive(Long idUser, Boolean isActive);

    public Page<User> findAllByIsActive(Boolean isActive, Pageable pageable);

    public Page<User> findByNameIgnoreCaseContainingAndIsActive(String keyword, Boolean isActive, Pageable pageable);

}
