package main;

import main.model.User;
import main.service.AccountManager;

public class Main {
    public static void main(String[] args) {
        // attempts to create test user account
        try{
            User firstUser = AccountManager.createAccount("test", "@test_1!", "first test user");

            if (firstUser != null) {
                System.out.println(firstUser);
            }
        }
        catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        // attempts to create second user account with duplicate username
        try {
            User duplicateUser = AccountManager.createAccount("test", "test123!", "duplicate username");

            if (duplicateUser == null) {
                System.out.println("\nUsername is already taken");
            }
        }
        catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        // attempts to create account for invalid user
        try {
            User invalidUser = AccountManager.createAccount("a", "test123!", "invalid username (too short)");
        }
        catch (IllegalArgumentException e) {
            System.out.println("\nException caught: " + e.getMessage());
        }
    }
}
