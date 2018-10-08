package com.future.OfficeInventorySystem.repository;

import com.future.OfficeInventorySystem.model.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestRepository extends JpaRepository<Request,Long> {

    Request findRequestByIdRequest(Long idRequest);
}
