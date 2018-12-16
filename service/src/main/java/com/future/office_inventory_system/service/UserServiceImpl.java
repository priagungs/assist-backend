package com.future.office_inventory_system.service;

import com.future.office_inventory_system.exception.ConflictException;
import com.future.office_inventory_system.exception.InvalidValueException;
import com.future.office_inventory_system.exception.NotFoundException;
import com.future.office_inventory_system.model.Request;
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
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserHasItemService userHasItemService;

    @Autowired
    private RequestService requestService;

    public User createUser(User user) {
        if (user.getSuperior().getIdUser() != null) {
            user.setSuperior(userRepository
                    .findByIdUserAndIsActive(user.getSuperior().getIdUser(), true)
                    .orElseThrow(() -> new NotFoundException("superior not found")));
        }
        if (userRepository.findByUsernameAndIsActive(user.getUsername(), true).isPresent()) {
            throw new ConflictException("username already exist");
        }

        return userRepository.save(user);
    }

    public User updateUser(User user) {
        User updatedUser = userRepository
                .findByIdUserAndIsActive(user.getIdUser(), true)
                .orElseThrow(() -> new NotFoundException("user not found"));

        if (user.getSuperior() != null) {
            for (User subordinate: updatedUser.getSubordinates()) {
                if (user.getSuperior().getIdUser() == subordinate.getIdUser()) {
                    throw new InvalidValueException("Subordinates can't be superior");
                }
            }
            updatedUser.setSuperior(userRepository
                    .findByIdUserAndIsActive(user.getSuperior().getIdUser(), true)
                    .orElseThrow(() -> new NotFoundException("superior not found")));
        }
        else {
            updatedUser.setSuperior(null);
        }



        updatedUser.setIsAdmin(user.getIsAdmin());
        updatedUser.setName(user.getName());
        if (userRepository.findByUsernameAndIsActive(user.getUsername(), true).isPresent() && !updatedUser.getUsername().equals(user.getUsername())) {
            throw new ConflictException("username already exist");
        }
        updatedUser.setUsername(user.getUsername());
        updatedUser.setPictureURL(user.getPictureURL());
        updatedUser.setPasswordWithoutEncode(user.getPassword());
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
        return userRepository.findAllBySuperiorAndIsActive(userRepository.findByIdUserAndIsActive(id, true)
                .orElseThrow(() -> new NotFoundException("superior not found")), true, pageable);
    }

    public Page<User> readAllUsersContaining(String keyword, Pageable pageable) {
        return userRepository.findByNameIgnoreCaseContainingAndIsActive(keyword, true, pageable);
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

        for (Request request : user.getRequests()) {
            requestService.deleteRequest(request);
        }

        user.setRequests(null);
        user.setHasItems(null);
        user.setIsActive(false);
        userRepository.save(user);

        return ResponseEntity.ok().build();

    }
}
