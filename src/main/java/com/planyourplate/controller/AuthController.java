package com.planyourplate.controller;

import com.planyourplate.entity.User;
import com.planyourplate.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.planyourplate.security.JwtUtil;

import java.util.Optional;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public AuthController(UserRepository userRepository, JwtUtil jwtUtil) {
    this.userRepository = userRepository;
    this.jwtUtil = jwtUtil;
}

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {

        Optional<User> existingUser =
                userRepository.findByEmail(user.getEmail());

        if (existingUser.isPresent()) {
            return ResponseEntity.badRequest()
                    .body("Email already registered");
        }

        user.setRole("USER");

        User savedUser = userRepository.save(user);

        savedUser.setPassword(null);

        return ResponseEntity.ok(savedUser);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {

        Optional<User> existingUser =
                userRepository.findByEmail(user.getEmail());

        if (existingUser.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body("Invalid email or password");
        }

        User foundUser = existingUser.get();

        if (!foundUser.getPassword().equals(user.getPassword())) {
            return ResponseEntity.badRequest()
                    .body("Invalid email or password");
        }

        foundUser.setPassword(null);
        String token = jwtUtil.generateToken(foundUser.getEmail());

return ResponseEntity.ok(
    java.util.Map.of(
        "token", token,
        "message", "Login successful",
        "email", foundUser.getEmail(),
        "name", foundUser.getName(),
        "role", foundUser.getRole()
    )
);

    
    }
}
