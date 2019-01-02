package com.future.assist.repository;

import com.future.assist.model.RequestStatus;
import com.future.assist.model.entity_model.Item;
import com.future.assist.model.entity_model.Request;
import com.future.assist.model.entity_model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

    public Optional<Request> findRequestByIdRequest(Long idRequest);

    public Page<Request> findAllRequestsByRequestBy(User user, Pageable pageable);

    public Page<Request> findAllRequestsByRequestStatus(RequestStatus requestStatus, Pageable pageable);

    public Page<Request> findAllByRequestByAndRequestStatus(User user, RequestStatus status, Pageable pageable);

    public List<Request> findAllRequestsByItem(Item item);

}
