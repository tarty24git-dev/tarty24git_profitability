package com.cth.tradefinance.controller;

import com.cth.tradefinance.model.SystemConfig;
import com.cth.tradefinance.model.User;
import com.cth.tradefinance.repository.SystemConfigRepository;
import com.cth.tradefinance.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final SystemConfigRepository configRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository,
                          SystemConfigRepository configRepository,
                          PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.configRepository = configRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login-rest")
    public ResponseEntity<?> restLogin(@RequestBody Map<String, String> payload) {
        String username = payload.get("username");
        String password = payload.get("password");

        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Invalid username or password"));
        }

        User user = userOpt.get();
        if (!user.isAccountNonLocked()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", "Account is locked. Please contact administrator."));
        }

        if (user.getPasswordExpirationDate() != null && user.getPasswordExpirationDate().isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", "Password expired. Please reset password."));
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            user.setFailedAttempts(user.getFailedAttempts() + 1);
            if (user.getFailedAttempts() >= 3) {
                user.setAccountNonLocked(false);
            }
            userRepository.save(user);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Invalid username or password"));
        }

        user.setFailedAttempts(0);
        userRepository.save(user);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "SUCCESS");
        response.put("username", user.getUsername());
        response.put("fullName", user.getFullName());
        response.put("role", user.getRole());
        response.put("twoFactorEnabled", user.isTwoFactorEnabled());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/config")
    public ResponseEntity<?> getPublicConfig() {
        Map<String, String> configs = new HashMap<>();
        configRepository.findAll().forEach(c -> configs.put(c.getConfigKey(), c.getConfigValue()));
        return ResponseEntity.ok(configs);
    }
}
