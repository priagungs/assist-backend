package com.future.office_inventory_system.service;

import com.future.office_inventory_system.exception.ConflictException;
import com.future.office_inventory_system.exception.NotFoundException;
import com.future.office_inventory_system.model.User;
import com.future.office_inventory_system.model.UserHasItem;
import com.future.office_inventory_system.repository.UserRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Data
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserHasItemService userHasItemService;

    public User createUser(User user) {
        user.setSuperior(userRepository
                .findByIdUserAndIsActive(user.getSuperior().getIdUser(), true)
                .orElseThrow(() -> new NotFoundException("superior not found")));

        return userRepository.save(user);

    }

    public User updateUser(User user) {
        User updatedUser = userRepository
                .findByIdUserAndIsActive(user.getIdUser(), true)
                .orElseThrow(() -> new NotFoundException("user not found"));

        user.setSuperior(userRepository
                .findByIdUserAndIsActive(user.getSuperior().getIdUser(), true)
                .orElseThrow(() -> new NotFoundException("superior not found")));

        updatedUser.setIsAdmin(user.getIsAdmin());
        updatedUser.setName(user.getName());
        updatedUser.setUsername(user.getUsername());
        updatedUser.setPictureURL(user.getPictureURL());
        updatedUser.setPassword(user.getPassword());
        updatedUser.setDivision(user.getDivision());
        updatedUser.setRole(user.getRole());

        return userRepository.save(updatedUser);
    }

    public Page<User> readAllUsers(Pageable pageable) {

        return userRepository.findAllByIsActive(true, pageable);
    }

    public User readUserByIdUser(Long id) {
        return userRepository.findByIdUserAndIsActive(id, true)
                .orElseThrow(() -> new NotFoundException("user not found"));
    }

    public Page<User> readAllUsersByIdSuperior(Long id, Pageable pageable) {
        return userRepository.findAllBySuperior(userRepository.findByIdUserAndIsActive(id, true)
                .orElseThrow(() -> new NotFoundException("superior not found")), pageable);
    }

    public Page<User> readAllUsersByIsAdmin(Boolean isAdmin, Pageable pageable) {
        return userRepository.findAllByIsAdminAndIsActive(isAdmin, true, pageable);
    }

    public User readUserByUsername(String username) {
        return userRepository.findByUsernameAndIsActive(username, true)
                .orElseThrow(() -> new NotFoundException("user not found"));
    }

    public ResponseEntity deleteUser(Long id) {
        User user = userRepository.findByIdUserAndIsActive(id, true)
                .orElseThrow(() -> new NotFoundException("user not found"));

        for (User subordinate: user.getSubordinates()) {
            subordinate.setSuperior(user.getSuperior());
        }

        for (UserHasItem hasItem : user.getHasItems()) {
            userHasItemService.deleteUserHasItem(hasItem.getIdUserHasItem());
        }

        user.setIsActive(false);
        userRepository.save(user);

        return ResponseEntity.ok().build();

    }
}
