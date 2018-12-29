package com.future.assist.service.service_interface;

import com.future.assist.model.entity_model.User;
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

    Page<User> readAllUsersContaining(String keyword, Pageable pageable);

    User readUserByUsername(String username);

    ResponseEntity deleteUser(Long id);

}
