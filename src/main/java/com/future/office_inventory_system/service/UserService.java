package com.future.office_inventory_system.service;

import com.future.office_inventory_system.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    User createUser(User user);
    
    User updateUser(User user);
    
    Page<User> readAllUsers(Pageable pageable);
    
    User readUserByIdUser(Long id);
    
    Page<User> readAllUsersByIdSuperior(Long id, Pageable pageable);
    
    Page<User> readAllUsersByIsAdmin(Boolean isAdmin, Pageable pageable);
    
    User readUserByUsername(String username);
    
    ResponseEntity deleteUser(Long id);

}
