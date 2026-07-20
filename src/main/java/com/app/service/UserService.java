package com.app.service;

import com.app.model.User;
import com.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User createUser(String username, String password, String description) {

        //BL
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username is required.");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password is required.");
        }

        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username is already taken.");
        }

        User newUser = new User(username, password, description);
        return userRepository.save(newUser);
    }

    public User login(String username, String password) {

        //use repository
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Incorrect username or password."));

        if (!user.getPassword().equals(password)) {
            throw new IllegalArgumentException("Incorrect username or password.");
        }

        return user;
    }

}