package com.app.service;

import com.app.model.User;
import com.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.http.client.enabled", havingValue = "false", matchIfMissing = true)
public class UserService implements UserUseCases {

    private final UserRepository userRepository;

    //user status
    private User loggedInUser = null;

    @Override
    public User createUser(String username, String email, String password, String description) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username is required.");
        }
        if (email == null || email.isBlank() || !email.contains("@")) {
            throw new IllegalArgumentException("A valid email is required.");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password is required.");
        }
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username is already taken.");
        }
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email is already taken.");
        }

        User newUser = new User(username, email, password, description);
        return userRepository.save(newUser);
    }

    @Override
    public User login(String identifier, String password) {
        // search for the user sending the same 'identifier' for both username and email
        User user = userRepository.findByUsernameOrEmail(identifier, identifier)
                .orElseThrow(() -> new IllegalArgumentException("Incorrect username/email or password."));

        if (!user.getPassword().equals(password)) {
            throw new IllegalArgumentException("Incorrect username/email or password.");
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
}