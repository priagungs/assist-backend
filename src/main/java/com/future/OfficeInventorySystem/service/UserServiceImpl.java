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
        User superior = userRepository.findByIdUser(user.getSuperior().getIdUser());

        if(userRepository.findByIdUser(user.getIdUser()) == null) {
            return false;
        }
        else if(superior == null) {
            return false;
        }
        else {
            if (!superior.getSubordinates().contains(user)) {
                superior.getSubordinates().add(user);
                userRepository.save(superior);
            }
            userRepository.save(user);
            return true;
        }
    }

    @Override
    public List<User> readAllUser() {
        return userRepository.findAll();
    }

    @Override
    public User readUserByIdUser(Long id) {
        return userRepository.findByIdUser(id);
    }

    @Override
    public List<User> readUserByIdSuperior(Long id) {
        return userRepository
                .findAllBySuperior(userRepository.findByIdUser(id), pageRequest)
                .getContent();
    }

    @Override
    public Boolean deleteUser(Long id) {
        User user = userRepository.findByIdUser(id);
        if(user == null) {
            return false;
        }
        else {
            if(user.getSuperior() != null) {
                User superior = userRepository.findByIdUser(user.getSuperior().getIdUser());
                superior.getSubordinates().remove(user);
                userRepository.save(superior);
            }
            if(user.getSubordinates() != null) {
                for (User u: user.getSubordinates()) {
                    u.setSuperior(null);
                    userRepository.save(u);
                }
            }
            userRepository.delete(user);
            return true;
        }
    }
}
