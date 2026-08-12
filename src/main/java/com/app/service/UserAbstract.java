package com.app.service;

import com.app.model.User;

public interface UserAbstract {
    User createUser(String username, String email, String password, String description);
    User createUser(String username, String email, String password);
    User login(String identifier, String password);
    void logout();
    User getLoggedInUser();
    User findByUsername(String username);
    User updateProfile(String username, String displayName, String avatarUrl);
    void changePassword(String username, String currentPassword, String newPassword);
    void deleteAccount(String username, String password);
}