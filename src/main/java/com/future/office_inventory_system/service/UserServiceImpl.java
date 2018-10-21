package com.future.office_inventory_system.service;

import com.future.office_inventory_system.exception.UserNotFoundException;
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


    public UserServiceImpl() {}


    public ResponseEntity createUser(User user) {
        user.setSuperior(userRepository
                .findById(user.getSuperior().getIdUser())
                .orElseThrow(() -> new UserNotFoundException("superior not found")));

        if (userRepository.findById(user.getIdUser()).isPresent()) {
            throw new RuntimeException(user.getIdUser().toString() + " is present");
        }

        userRepository.save(user);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity updateUser(User user) {
        User userBeforeUpdate = userRepository
                .findById(user.getIdUser())
                .orElseThrow(() -> new UserNotFoundException("user not found"));

        user.setSuperior(userRepository
                .findById(user.getSuperior().getIdUser())
                .orElseThrow(() -> new UserNotFoundException("superior not found")));

        userBeforeUpdate.setIsAdmin(user.getIsAdmin());
        userBeforeUpdate.setName(user.getName());
        userBeforeUpdate.setUsername(user.getUsername());
        userBeforeUpdate.setPictureURL(user.getPictureURL());
        userBeforeUpdate.setPassword(user.getPassword());
        userBeforeUpdate.setDivision(user.getDivision());
        userBeforeUpdate.setRole(user.getRole());

        userRepository.save(userBeforeUpdate);
        return ResponseEntity.ok().build();

    }

    public Page<User> readAllUsers(Pageable pageable) {

        return userRepository.findAll(pageable);
    }

    public User readUserByIdUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("user not found"));
    }

    public Page<User> readAllUsersByIdSuperior(Long id, Pageable pageable) {
        return userRepository.findAllBySuperior(userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("superior not found")), pageable);
    }

    public Page<User> readAllUsersByIsAdmin(Boolean isAdmin, Pageable pageable) {
        return userRepository.findAllByIsAdmin(isAdmin, pageable);
    }

    public User readUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("user not found"));
    }

    public ResponseEntity deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("user not found"));

        for (User subordinate: user.getSubordinates()) {
            subordinate.setSuperior(user.getSuperior());
            userRepository.save(subordinate);
        }

        for (UserHasItem hasItem : user.getHasItems()) {
            userHasItemService.deleteUserHasItem(hasItem.getIdUserHasItem());
        }

        user.setActive(false);
        userRepository.save(user);

        return ResponseEntity.ok().build();

    }
}
