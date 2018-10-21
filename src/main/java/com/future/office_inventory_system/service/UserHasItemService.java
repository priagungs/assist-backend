package com.future.office_inventory_system.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface UserHasItemService {
    ResponseEntity deleteUserHasItem(Long id);
}
