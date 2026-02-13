package com.chatapp.controller;

import com.chatapp.dto.LoginRequest;
import com.chatapp.dto.RegisterRequest;
import com.chatapp.repository.UserRepository;
import com.chatapp.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        try {
            String message = userService.registerUser(request);

            Map<String, String> response = new HashMap<>();
            response.put("message", message);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", e.getMessage())
            );
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            String token = userService.loginUser(request);

            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            response.put("username", request.getUsername());
            response.put("message", "Login successful");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", "Invalid credentials")
            );
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestParam String username) {
        try {
            userService.updateOnlineStatus(username, false);
            return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", e.getMessage())
            );
        }
    }

    /**
     * Check if username exists - for private chat validation
     */
    @GetMapping("/user/exists")
    public ResponseEntity<?> checkUserExists(@RequestParam String username) {
        boolean exists = userRepository.existsByUsername(username);

        if (exists) {
            return ResponseEntity.ok(Map.of(
                    "exists", true,
                    "username", username
            ));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "exists", false,
                    "error", "User not found"
            ));
        }
    }
}