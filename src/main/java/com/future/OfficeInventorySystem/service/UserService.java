package com.future.OfficeInventorySystem.service;

import com.future.OfficeInventorySystem.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

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
