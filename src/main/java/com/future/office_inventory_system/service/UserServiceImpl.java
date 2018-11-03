package com.future.office_inventory_system.service;

import com.future.office_inventory_system.exception.ConflictException;
import com.future.office_inventory_system.exception.NotFoundException;
import com.future.office_inventory_system.model.User;
import com.future.office_inventory_system.model.UserHasItem;
import com.future.office_inventory_system.repository.UserRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Data
@EnableConfigurationProperties(UserServiceProperties.class)
public class UserServiceImpl implements UserService {

    public final UserServiceProperties userServiceProperties;

    public User createUser(User user) {
        if (user.getSuperior() != null) {
            user.setSuperior(userServiceProperties.getUserRepository()
                    .findByIdUserAndIsActive(user.getSuperior().getIdUser(), true)
                    .orElseThrow(() -> new NotFoundException("superior not found")));
        }
        if (userServiceProperties.getUserRepository().findByUsernameAndIsActive(user.getUsername(), true).isPresent()) {
            throw new ConflictException("username already exist");
        }

        return userServiceProperties.getUserRepository().save(user);
    }

    public User updateUser(User user) {
        User updatedUser = userServiceProperties.getUserRepository()
                .findByIdUserAndIsActive(user.getIdUser(), true)
                .orElseThrow(() -> new NotFoundException("user not found"));

        updatedUser.setSuperior(userServiceProperties.getUserRepository()
                .findByIdUserAndIsActive(user.getSuperior().getIdUser(), true)
                .orElseThrow(() -> new NotFoundException("superior not found")));

        updatedUser.setIsAdmin(user.getIsAdmin());
        updatedUser.setName(user.getName());
        if (userServiceProperties.getUserRepository().findByUsernameAndIsActive(user.getUsername(), true).isPresent() && !updatedUser.getUsername().equals(user.getUsername())) {
            throw new ConflictException("username already exist");
        }
        updatedUser.setUsername(user.getUsername());
        updatedUser.setPictureURL(user.getPictureURL());
        updatedUser.setPassword(user.getPassword());
        updatedUser.setDivision(user.getDivision());
        updatedUser.setRole(user.getRole());

        return userServiceProperties.getUserRepository().save(updatedUser);
    }

    public Page<User> readAllUsers(Pageable pageable) {

        return userServiceProperties.getUserRepository().findAllByIsActive(true, pageable);
    }

    public User readUserByIdUser(Long id) {
        return userServiceProperties.getUserRepository().findByIdUserAndIsActive(id, true)
                .orElseThrow(() -> new NotFoundException("user not found"));
    }

    public Page<User> readAllUsersByIdSuperior(Long id, Pageable pageable) {
        return userServiceProperties.getUserRepository().findAllBySuperiorAndIsActive(userServiceProperties.getUserRepository().findByIdUserAndIsActive(id, true)
                .orElseThrow(() -> new NotFoundException("superior not found")), true, pageable);
    }

    public User readUserByUsername(String username) {
        return userServiceProperties.getUserRepository().findByUsernameAndIsActive(username, true)
                .orElseThrow(() -> new NotFoundException("user not found"));
    }

    public ResponseEntity deleteUser(Long id) {
        User user = userServiceProperties.getUserRepository().findByIdUserAndIsActive(id, true)
                .orElseThrow(() -> new NotFoundException("user not found"));

        for (User subordinate: user.getSubordinates()) {
            subordinate.setSuperior(user.getSuperior());
        }

        for (UserHasItem hasItem : user.getHasItems()) {
            userServiceProperties.getUserHasItemService().deleteUserHasItem(hasItem.getIdUserHasItem());
        }

        user.setIsActive(false);
        userServiceProperties.getUserRepository().save(user);

        return ResponseEntity.ok().build();

    }
}
