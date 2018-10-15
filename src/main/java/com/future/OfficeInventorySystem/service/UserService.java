package com.future.OfficeInventorySystem.service;

import com.future.OfficeInventorySystem.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    Boolean createUser(User user);
    Boolean updateUser(User user);
    List<User> readAllUser();
    User readUserByIdUser(Long id);
    List<User> readUserByIdSuperior(Long id);
    Boolean deleteUser(Long id);

}
