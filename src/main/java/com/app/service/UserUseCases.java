package com.app.service;

import com.app.model.User;

public interface UserUseCases {
    User createUser(String username, String email, String password);
    User findByUsername(String username);
    User updateProfile(String username, String displayName, String avatarUrl);
    void changePassword(String username, String currentPassword, String newPassword);
}