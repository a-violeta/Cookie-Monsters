package com.app.service;

import com.app.model.User;

public interface UserUseCases {
    User createUser(String username, String email, String password, String description);
    User login(String username, String password);
    void logout();
    User getLoggedInUser();
    //User findUserById(long userId);
    //User findUserByUsername(String username);
    //void editUser(long userId, ...);
    // + more methods
}
