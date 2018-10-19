package com.future.OfficeInventorySystem.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface UserHasItemService {
    ResponseEntity deleteUserHasItem(Long id);
}
