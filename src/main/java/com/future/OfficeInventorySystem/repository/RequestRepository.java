package com.future.OfficeInventorySystem.repository;

import com.future.OfficeInventorySystem.model.Request;
import com.future.OfficeInventorySystem.model.Status;
import com.future.OfficeInventorySystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request,Long> {

    Request findRequestByIdRequest(Long idRequest);
    List<Request> findAllRequestByUser(User user);
    List<Request> findAllRequestByStatus(Status status);
    List<Request> findAllRequestByStatusAndSuperior(Status status, User superior);

}
