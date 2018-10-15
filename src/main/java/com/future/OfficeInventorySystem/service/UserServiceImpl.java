package com.future.OfficeInventorySystem.service;

import com.future.OfficeInventorySystem.model.User;
import com.future.OfficeInventorySystem.repository.UserRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Data
public class UserServiceImpl implements UserService {

    private PageRequest pageRequest;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Boolean createUser(User user) {
        if(userRepository.findByIdUser(user.getIdUser()) != null ||
                userRepository.findByUsername(user.getUsername()) != null) {
            return false;
        }
        else if(user.getSuperior() != null &&
                userRepository.findByIdUser(user.getSuperior().getIdUser()) == null) {
            return false;
        }
        else {
            User superior = userRepository.findByIdUser(user.getSuperior().getIdUser());
            superior.getSubordinates().add(user);
            userRepository.save(superior);
            userRepository.save(user);
            return true;
        }
    }

    @Override
    public Boolean updateUser(User user) {
        if(userRepository.findByIdUser(user.getIdUser()) == null) {
            return false;
        }
    }

    @Override
    public List<User> readAllUser() {
        return null;
    }

    @Override
    public List<User> readUserByIdUser(Long id) {
        return null;
    }

    @Override
    public List<User> readUserByIdSuperior(Long id) {
        return null;
    }

    @Override
    public Boolean deleteUser(Long id) {
        return null;
    }
}
