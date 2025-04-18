package com.th.test0804.service;

import com.th.test0804.entity.User;
import com.th.test0804.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private  UserRepository userRepository;


    public User createUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email is already taken.");
        }
        if (userRepository.findByFirstname(user.getFirstname()).isPresent()) {
            throw new IllegalArgumentException("User is already taken.");
        }
        return userRepository.save(user);
    }

    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);  // This will automatically handle pagination
    }

    public User getUserById(UUID id) {
        return userRepository.findById(id)
                .orElse(null);
    }

    public User updateUser(UUID id, User updatedUser) {
        if (userRepository.findByEmail(updatedUser.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email is already taken.");
        }
        if (userRepository.findByFirstname(updatedUser.getFirstname()).isPresent()) {
            throw new IllegalArgumentException("User is already taken.");
        }
        User user = getUserById(id);
        user.setFirstname(updatedUser.getFirstname());
        user.setEmail(updatedUser.getEmail());
        user.setRole(updatedUser.getRole());
        return userRepository.save(user);
    }

    public boolean deleteUser(UUID id) {
        Boolean deleted;
        try {
            userRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
