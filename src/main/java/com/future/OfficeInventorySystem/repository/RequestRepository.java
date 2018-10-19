package com.future.OfficeInventorySystem.repository;

import com.future.OfficeInventorySystem.model.Request;
import com.future.OfficeInventorySystem.model.Status;
import com.future.OfficeInventorySystem.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface RequestRepository extends JpaRepository<Request,Long> {

    Page<Request> findAllRequestByUser(User user, Pageable pageable);
    Page<Request> findAllRequestByStatus(Status status, Pageable pageable);
    Page<Request> findAllRequestByStatusAndSuperior(Status status, User superior, Pageable pageable);

}
