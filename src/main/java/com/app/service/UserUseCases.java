package com.app.service;

import com.app.model.User;

public interface UserUseCases {
    User addUser(User user);
    void deleteUser(long userId);
    void listUsers();
    //User findUserById(long userId);
    //User findUserByUsername(String username);
    //void editUser(long userId, ...);
    // + more methods
}
