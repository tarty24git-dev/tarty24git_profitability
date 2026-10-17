package com.cth.tradefinance.controller;

import com.cth.tradefinance.model.SystemConfig;
import com.cth.tradefinance.model.User;
import com.cth.tradefinance.repository.SystemConfigRepository;
import com.cth.tradefinance.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final SystemConfigRepository configRepository;

    public UserController(UserService userService, SystemConfigRepository configRepository) {
        this.userService = userService;
        this.configRepository = configRepository;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User created = userService.createUser(user);
        return ResponseEntity.ok(created);
    }

    @PostMapping("/{id}/lock")
    public ResponseEntity<Map<String, String>> lockUser(@PathVariable Long id) {
        userService.lockUser(id);
        return ResponseEntity.ok(Map.of("message", "User locked successfully"));
    }

    @PostMapping("/{id}/unlock")
    public ResponseEntity<Map<String, String>> unlockUser(@PathVariable Long id) {
        userService.unlockUser(id);
        return ResponseEntity.ok(Map.of("message", "User unlocked successfully"));
    }

    @PostMapping("/{id}/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String newPassword = body.getOrDefault("newPassword", "Qwerty123$");
        userService.resetPassword(id, newPassword);
        return ResponseEntity.ok(Map.of("message", "Password reset successfully"));
    }

    @PostMapping("/{id}/toggle-2fa")
    public ResponseEntity<Map<String, String>> toggle2FA(@PathVariable Long id, @RequestBody Map<String, Boolean> body) {
        boolean enabled = body.getOrDefault("enabled", false);
        userService.toggle2FA(id, enabled);
        return ResponseEntity.ok(Map.of("message", "2FA updated successfully"));
    }

    @PostMapping("/settings")
    public ResponseEntity<Map<String, String>> updateSettings(@RequestBody Map<String, String> settings) {
        settings.forEach((key, value) -> {
            SystemConfig cfg = configRepository.findById(key)
                    .orElse(new SystemConfig(key, value, "Updated from frontend"));
            cfg.setConfigValue(value);
            configRepository.save(cfg);
        });
        return ResponseEntity.ok(Map.of("message", "Settings updated successfully"));
    }
}
