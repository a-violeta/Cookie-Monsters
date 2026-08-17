package com.app.service;

import com.app.exception.DuplicateResourceException;
import com.app.model.User;
import com.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.http.client.enabled", havingValue = "false", matchIfMissing = true)
public class UserService implements UserAbstract {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public User createUser(String username, String email, String password, String description) {
        // one query instead of two separate exists checks - findByUsernameOrEmail was
        // already defined but unused; this halves the DB round-trips on every registration
        userRepository.findByUsernameOrEmail(username, email).ifPresent(existing -> {
            if (existing.getUsername().equals(username)) {
                throw new DuplicateResourceException("Username is already taken");
            }
            throw new DuplicateResourceException("Email is already taken");
        });

        User user = new User(username, email, passwordEncoder.encode(password), description);
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User createUser(String username, String email, String password) {
        return createUser(username, email, password, "New user");
    }

    @Override
    @Transactional(readOnly = true)
    public User getLoggedInUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            return null; // No user is logged in
        }

        //JWT filter places the username in the authentication principal
        String username = auth.getName();
        return findByUsername(username);
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

    @Override
    @Transactional
    public void deleteAccount(String username, String password) {
        User user = findByUsername(username);
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("Incorrect password");
        }
        // @SQLDelete on User intercepts this and converts it into
        // "UPDATE app_users SET is_deleted = true" automatically - no manual flag flip needed
        userRepository.delete(user);
    }

    @Override
    public User login(String identifier, String password) {
        User user = userRepository.findByUsername(identifier)
                .or(() -> userRepository.findByEmail(identifier))
                .orElseThrow(() -> new IllegalArgumentException("Invalid username/email or password"));

        if (user.isDeleted()) {
            throw new IllegalArgumentException("This account has been deleted");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("Invalid username/email or password");
        }

        // inject the user into the local spring security context for the CLI/seeddata
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(user.getUsername(), null, new java.util.ArrayList<>());
        SecurityContextHolder.getContext().setAuthentication(authToken);

        return user;
    }

    @Override
    public void logout() {
        SecurityContextHolder.clearContext();
    }
}