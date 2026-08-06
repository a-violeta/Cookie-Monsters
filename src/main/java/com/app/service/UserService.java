package com.app.service;

import com.app.model.User;
import com.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.http.client.enabled", havingValue = "false", matchIfMissing = true)
public class UserService implements UserUseCases {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Local session cache for non-HTTP (database/CLI) mode
    private User loggedInUser = null;

    @Override
    @Transactional
    public User createUser(String username, String email, String password, String description) {
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username is already taken");
        }
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email is already taken");
        }
        User user = new User(
                username,
                email,
                passwordEncoder.encode(password),
                description
        );
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User createUser(String username, String email, String password) {
        return createUser(username, email, password, "New user");
    }

    @Override
    public User login(String identifier, String password) {
        User user = userRepository.findByUsername(identifier)
                .or(() -> userRepository.findByEmail(identifier))
                .orElseThrow(() -> new IllegalArgumentException("Invalid username/email or password"));

        if (!user.getPassword().equals(password)) {
            throw new IllegalArgumentException("Invalid username/email or password");
        }

        this.loggedInUser = user;
        return user;
    }

    @Override
    public void logout() {
        this.loggedInUser = null;
    }

    @Override
    public User getLoggedInUser() {
        return this.loggedInUser;
    }

    @Override
    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User with username " + username + " not found"));
    }

    @Override
    @Transactional
    public User updateProfile(String username, String displayName, String avatarUrl) {
        User user = findByUsername(username);
        if (displayName != null) {
            user.setDisplayName(displayName);
        }
        if (avatarUrl != null) {
            user.setAvatarUrl(avatarUrl);
        }
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void changePassword(String username, String currentPassword, String newPassword) {
        User user = findByUsername(username);
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}