package com.future.office_inventory_system.repository;

import com.future.office_inventory_system.model.Request;
import com.future.office_inventory_system.model.RequestStatus;
import com.future.office_inventory_system.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestRepository extends JpaRepository<Request,Long> {

    Page<Request> findAllRequestByIdUser(Long idUser, Pageable pageable);
    
    Page<Request> findAllRequestByStatus(RequestStatus requestStatus, Pageable pageable);

}
