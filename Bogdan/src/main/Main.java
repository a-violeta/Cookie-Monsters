package main;

import main.model.User;
import main.service.AccountManager;
import main.service.Logger;

public class Main {
    public static void main(String[] args) {
        // attempts to create test user account
        try {
            User firstUser = AccountManager.createAccount("test", "@test_1!", "first test user");

            if (firstUser != null) {
                System.out.println(firstUser);
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        // attempts to create second user account with duplicate username
        try {
            User duplicateUser = AccountManager.createAccount("test", "test123!", "duplicate username");

            if (duplicateUser == null) {
                System.out.println("\nUsername is already taken");
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        // attempts to create account for invalid user
        try {
            User invalidUser = AccountManager.createAccount("a", "test123!", "invalid username (too short)");
        } catch (IllegalArgumentException e) {
            System.out.println("\nException caught: " + e.getMessage());
        }

        // tests login and logout
        try {
            User testUser = AccountManager.createAccount("test_logging", "test123!", "test user login");

            if (testUser != null) {
                Logger.logIn(testUser.getUsername(), testUser.getPassword());
                System.out.println("\nActive user: " + Logger.getActiveUser());

                Logger.logOut(testUser.getUsername());
                System.out.println("\nActive user: " + Logger.getActiveUser());
            } else {
                System.out.println("\nUsername is already taken");

            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}
