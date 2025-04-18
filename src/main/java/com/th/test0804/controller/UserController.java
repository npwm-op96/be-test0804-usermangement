package com.th.test0804.controller;

import com.th.test0804.dto.AuthState;
import com.th.test0804.dto.UserRequest;
import com.th.test0804.entity.User;
import com.th.test0804.models.ApiPageResponse;
import com.th.test0804.repository.UserRepository;
import com.th.test0804.service.UserService;
import com.th.test0804.models.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:4200")

public class UserController {

    private final UserService userService;
    @Autowired
    private UserRepository userRepository;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    @PostMapping("/login")
    public ApiResponse<AuthState> login(@RequestHeader("x-test-token") String xTestAuth, @RequestBody UserRequest userReq) {
        if ("test0804".equals(xTestAuth) && "admin@gmail.com".equals(userReq.getEmail())) {
            Optional<User> acc = userRepository.findByEmail(userReq.getEmail());
            if (acc.isPresent()) {
                User users = acc.get();
                UUID token = UUID.randomUUID();
                AuthState authState = AuthState.builder().user(users).token(String.valueOf(token)).build();
                ApiResponse<AuthState> response = ApiResponse.success("200",authState, "User login successfully.");
                return response;
            }
        } else {
            AuthState authState = AuthState.builder().error("User login not successfully").build();
            ApiResponse<AuthState> response = ApiResponse.error("401", authState, "User login not successfully.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response).getBody();
        }
        return null;
    }
    // Create user
    @PostMapping
    public ResponseEntity<ApiResponse<User>> createUser(@RequestBody User user) {
        try {
            User createdUser = userService.createUser(user);
            ApiResponse<User> response = ApiResponse.success("200", createdUser, "User created successfully.");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException ex) {
            ApiResponse<User> response = ApiResponse.error("400",null ,ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    // Get all users
    @GetMapping("/list")
    public ResponseEntity<ApiPageResponse<List<User>>> getAllUsers(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        // Create Pageable instance with page and size
        Pageable pageable = PageRequest.of(page, size);

        // Fetch the page of users
        Page<User> userPage = userService.getAllUsers(pageable);

        // Prepare the response with success
        ApiPageResponse<List<User>> response = ApiPageResponse.success(
                "200",
                userPage.getContent(),  // Get list of users from the Page object
                "Users fetched successfully.",
                userPage.getTotalElements()  // Total number of users
        );

        return ResponseEntity.ok(response);
    }
    // Get user by ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> getUserByIds(@PathVariable UUID id) {
        User user = userService.getUserById(id);
        if (user != null) {
            ApiResponse<User> response = ApiResponse.success("200", user, "User found.");
            return ResponseEntity.ok(response);
        } else {
            ApiResponse<User> response = ApiResponse.error("404", null,"User not found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    // Update user
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> updateUser(@PathVariable UUID id, @RequestBody User user) {
        User user1 = userService.getUserById(id);
        if (user1 == null) {
            ApiResponse<User> response = ApiResponse.error("404",null, "User Not Request Delete.");
            return (ResponseEntity<ApiResponse<User>>) ResponseEntity.badRequest();
        }
        User updatedUser = userService.updateUser(id, user);
        if (updatedUser != null) {
            ApiResponse<User> response = ApiResponse.success("200", updatedUser, "User updated successfully.");
            return ResponseEntity.ok(response);
        } else {
            ApiResponse<User> response = ApiResponse.error("404",null, "User not found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    // Delete user
    @DeleteMapping("/{id}")
    public ApiResponse<User> deleteUser(@PathVariable UUID id) {
        User user1 = userService.getUserById(id);
        if (user1 == null) {
            return ApiResponse.error("404",null, "User Not Request Update.");
        }
        boolean deleted = userService.deleteUser(id);
        ApiResponse<User> response;
        if (deleted) {
            response = ApiResponse.success("200", null, "User deleted successfully.");
        } else {
            response = ApiResponse.error("404",null, "User not found.");
        }
        return response;
    }
}