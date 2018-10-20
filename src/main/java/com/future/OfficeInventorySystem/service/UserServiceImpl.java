package com.future.OfficeInventorySystem.service;

import com.future.OfficeInventorySystem.exception.UserNotFoundException;
import com.future.OfficeInventorySystem.model.User;
import com.future.OfficeInventorySystem.model.UserHasItem;
import com.future.OfficeInventorySystem.repository.UserHasItemRepository;
import com.future.OfficeInventorySystem.repository.UserRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Data
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserHasItemRepository userHasItemRepository;

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

        return ResponseEntity.ok().build();

    }

    public List<User> readAllUser(Pageable pageable) {
        return userRepository.findAll(pageable).getContent();
    }

    public User readUserByIdUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("user not found"));
    }

    public List<User> readUserByIdSuperior(Long id, Pageable pageable) {
        return userRepository.findAllBySuperior(userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("superior not found")), pageable)
                .getContent();
    }

    public List<User> readUserByIsAdmin(Boolean isAdmin, Pageable pageable) {
        return userRepository.findAllByIsAdmin(isAdmin, pageable).getContent();
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

        return ResponseEntity.ok().build();

    }
}
