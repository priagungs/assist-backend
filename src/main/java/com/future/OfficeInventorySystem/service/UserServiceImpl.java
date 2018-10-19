package com.future.OfficeInventorySystem.service;

import com.future.OfficeInventorySystem.model.User;
import com.future.OfficeInventorySystem.repository.UserHasItemRepository;
import com.future.OfficeInventorySystem.repository.UserRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Data
public class UserServiceImpl implements UserService {

    private Pageable pageRequest;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserHasItemRepository userHasItemRepository;

    public UserServiceImpl(PageRequest pageRequest) {
        this.pageRequest = pageRequest;
    }

    public UserServiceImpl() {}

    public Boolean createUser(User user) {
        if(userRepository.findByIdUser(user.getIdUser()) != null) {
            return false;
        }
        else if(user.getSuperior() != null &&
                userRepository.findByIdUser(user.getSuperior().getIdUser()) == null) {
            return false;
        }
        else {
            if(user.getSuperior() != null) {
                User superior = userRepository.findByIdUser(user.getSuperior().getIdUser());
                superior.getSubordinates().add(user);
                userRepository.save(superior);
            }
            user.setSuperior(userRepository.findByIdUser(user.getSuperior().getIdUser()));
            userRepository.save(user);
            return true;
        }
    }

    public Boolean updateUser(User user) {
        if(userRepository.findByIdUser(user.getIdUser()) == null) {
            return false;
        }
        else if(user.getSuperior() != null &&
                userRepository.findByIdUser(user.getSuperior().getIdUser()) == null) {
            return false;
        }
        else {
            User userBeforeUpdate = userRepository.findByIdUser(user.getIdUser());
            if(userBeforeUpdate.getSuperior() != null && user.getSuperior() == null) {
                User superior = userBeforeUpdate.getSuperior();
                superior.getSubordinates().remove(user);
                userRepository.save(superior);
            }
            else if(userBeforeUpdate.getSuperior() != null && user.getSuperior() != null) {
                User recentSuperior = userBeforeUpdate.getSuperior();
                User newSuperior = userRepository.findByIdUser(user.getSuperior().getIdUser());

//                recentSuperior.getSubordinates().remove()
            }
        }
        return false;
    }

    public List<User> readAllUser() {
        return userRepository.findAll(pageRequest).getContent();
    }

    public User readUserByIdUser(Long id) {
        return userRepository.findByIdUser(id);
    }

    public List<User> readUserByIdSuperior(Long id) {
        return userRepository
                .findAllBySuperior(userRepository.findByIdUser(id), pageRequest)
                .getContent();
    }

    public List<User> readUserByIsAdmin(Boolean isAdmin) {
        return userRepository.findAllByIsAdmin(isAdmin, pageRequest).getContent();
    }

    public User readUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Boolean deleteUser(Long id) {
        User user = userRepository.findByIdUser(id);
        if(user == null) {
            return false;
        }
        else {
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
