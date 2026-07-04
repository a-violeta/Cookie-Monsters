package main.service;

import main.model.User;

import java.util.ArrayList;
import java.util.List;

public class Logger {
    // list of logged users to easily count active users in communities
    private static List<User> loggedUsers = new ArrayList<>();

    private static User activeUser;

    private Logger() {} // utility class

    public static List<User> getLoggedUsers() {
        return loggedUsers;
    }

    public static User getActiveUser() {
        return activeUser;
    }

    public static void logIn(String username, String password) {
        User loggingUser = AccountManager.getUserByUsername(username);

        if (loggingUser != null) {
            if (loggingUser.getPassword().equals(password)) {
                loggedUsers.add(loggingUser);
                activeUser = loggingUser;
            }
        }
    }

    public static void logOut(String username) {
        User loggedUser = AccountManager.getUserByUsername(username);


        if (loggedUser != null) {
            loggedUsers.remove(loggedUser);

            // reference equality is enough because AccountManager ensures unique instances per user
            if (activeUser != null && activeUser.equals(loggedUser)) {
                activeUser = null;
            }
        }
    }
}
