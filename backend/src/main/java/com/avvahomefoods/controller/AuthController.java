package com.avvahomefoods.controller;

import com.avvahomefoods.model.User;
import com.avvahomefoods.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            return ResponseEntity.badRequest().body("Error: Email is already in use!");
        }

        // Create new user's account
        User newUser = new User(user.getUsername(),
                passwordEncoder.encode(user.getPassword()),
                user.getName(),
                "USER");

        userRepository.save(newUser);
        return ResponseEntity.ok("User registered successfully!");
    }

    @PostMapping("/admin/register")
    public ResponseEntity<?> registerAdmin(@RequestBody User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            return ResponseEntity.badRequest().body("Error: Email is already in use!");
        }

        User newAdmin = new User(user.getUsername(),
                passwordEncoder.encode(user.getPassword()),
                user.getName(),
                "ADMIN");

        userRepository.save(newAdmin);
        return ResponseEntity.ok("Admin registered successfully!");
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody Map<String, String> loginRequest) {
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");

        User user = userRepository.findByUsername(username)
                .orElse(null);

        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            Map<String, Object> response = new HashMap<>();
            response.put("id", user.getId());
            response.put("username", user.getUsername());
            response.put("name", user.getName());
            response.put("role", user.getRole());
            response.put("token", "dummy-token-for-now"); // In real app, use JWT
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body("Error: Invalid username or password!");
        }
    }
}
