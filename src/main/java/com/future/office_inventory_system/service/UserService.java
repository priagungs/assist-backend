package com.future.office_inventory_system.service;

import com.future.office_inventory_system.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    ResponseEntity createUser(User user);
    
    ResponseEntity updateUser(User user);
    
    Page<User> readAllUser(Pageable pageable);
    
    User readUserByIdUser(Long id);
    
    Page<User> readUserByIdSuperior(Long id, Pageable pageable);
    
    Page<User> readUserByIsAdmin(Boolean isAdmin, Pageable pageable);
    
    User readUserByUsername(String username);
    
    ResponseEntity deleteUser(Long id);

}
