package main.service;

import main.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AccountManager {
    private static List<User> applicationUsers = new ArrayList<>();

    // private constructor to prevent instantiation (utility class)
    private AccountManager() {}

    public static User createAccount(String username, String password, String description) {
        for (User user : applicationUsers) {
            if (Objects.equals(user.getUsername(), username)) {
                throw new IllegalArgumentException("Username is already taken");
            }
        }

        User newUser = new User(username, password, description);
        applicationUsers.add(newUser);

        return newUser;
    }

    public static User getUserByUsername(String username) {
        for (User user : applicationUsers) {
            if (Objects.equals(user.getUsername(), username)) {
                return user;
            }
        }

        return null;
    }
}
