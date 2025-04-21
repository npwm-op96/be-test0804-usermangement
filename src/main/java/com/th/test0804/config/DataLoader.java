package com.th.test0804.config;

import com.th.test0804.dto.Role;
import com.th.test0804.entity.User;
import com.th.test0804.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;

    public DataLoader(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Check if the database already contains users
            Optional<User> existingUser1 = userRepository.findByEmail("admin@gmail.com");
            if (existingUser1.isEmpty()) {
                User user1 = new User();
                user1.setFirstname("Admin User");
                user1.setEmail("admin@gmail.com");
                user1.setRole(Role.ADMIN);
                userRepository.save(user1);
                System.out.println("Created user: " + user1.getEmail());
            } else {
                System.out.println("User with email admin1@gmail.com already exists");
            }
    }
}
