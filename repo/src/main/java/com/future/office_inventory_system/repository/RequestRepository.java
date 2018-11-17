package com.future.office_inventory_system.repository;

import com.future.office_inventory_system.model.Item;
import com.future.office_inventory_system.model.Request;
import com.future.office_inventory_system.model.RequestStatus;
import com.future.office_inventory_system.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<Request,Long> {

    Optional<Request> findRequestByIdRequest(Long idRequest);
    Page<Request> findAllRequestsByRequestBy(User user, Pageable pageable);
    Page<Request> findAllRequestsByRequestStatus(RequestStatus requestStatus, Pageable pageable);
    Page<Request> findAllByRequestByAndRequestStatus(User user, RequestStatus status, Pageable pageable);
    List<Request> findAllRequestsByItem(Item item);
    
}
