package com.cth.tradefinance.service;

import com.cth.tradefinance.model.User;
import com.cth.tradefinance.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setPasswordExpirationDate(LocalDateTime.now().plusDays(90));
        return userRepository.save(user);
    }

    public void lockUser(Long userId) {
        userRepository.findById(userId).ifPresent(user -> {
            user.setAccountNonLocked(false);
            userRepository.save(user);
        });
    }

    public void unlockUser(Long userId) {
        userRepository.findById(userId).ifPresent(user -> {
            user.setAccountNonLocked(true);
            user.setFailedAttempts(0);
            userRepository.save(user);
        });
    }

    public void resetPassword(Long userId, String newPassword) {
        userRepository.findById(userId).ifPresent(user -> {
            user.setPassword(passwordEncoder.encode(newPassword));
            user.setPasswordExpirationDate(LocalDateTime.now().plusDays(90));
            user.setFailedAttempts(0);
            user.setAccountNonLocked(true);
            userRepository.save(user);
        });
    }

    public void toggle2FA(Long userId, boolean enabled) {
        userRepository.findById(userId).ifPresent(user -> {
            user.setTwoFactorEnabled(enabled);
            userRepository.save(user);
        });
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
