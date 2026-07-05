package main;

import main.model.Comment;
import main.model.Community;
import main.model.Post;
import main.model.User;
import main.service.AccountManager;
import main.service.CommunityManager;
import main.service.Logger;

public class Main {
    public static void main(String[] args) {
        // attempts to create test user account
        try {
            User firstUser = AccountManager.createAccount("test", "@test_1!", "first test user");
            System.out.println(firstUser);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        // attempts to create second user account with duplicate username
        try {
            User duplicateUser = AccountManager.createAccount("test", "test123!", "duplicate username");
        } catch (IllegalArgumentException e) {
            System.out.println("\nException caught: " + e.getMessage());
        }

        // attempts to create account for invalid user
        try {
            User invalidUser = AccountManager.createAccount("a", "test123!", "invalid username (too short)");
        } catch (IllegalArgumentException e) {
            System.out.println("\nException caught: " + e.getMessage());
        }

        // tests login and logout
        try {
            User testCreator = AccountManager.createAccount("test_logging", "test123!", "test community creator");

            Logger.logIn(testCreator.getUsername(), testCreator.getPassword());
            System.out.println("\nActive user: " + Logger.getActiveUser());

            // tests community creation
            Community testCommunity = CommunityManager.createCommunity("Test", "Test community");
            System.out.println(testCommunity);

            // attempts to create community with duplicate name
            try {
                Community duplicateCommunity = CommunityManager.createCommunity("Test", "Duplicate community");
            } catch (IllegalArgumentException e) {
                System.out.println("\nException caught: " + e.getMessage());
            }

            User testMember = AccountManager.createAccount("test_member", "@test_1!", "test community member");

            // tests adding members to community
            User newMember = testCommunity.addMember(testMember);

            // attempts to create community when user is not logged in
            Logger.logOut(testCreator.getUsername());

            try {
                Community invalidCommunity = CommunityManager.createCommunity("Test2", "Invalid community creator");
            } catch (IllegalArgumentException e) {
                System.out.println("\nException caught: " + e.getMessage());
            }

            // tests adding and removing posts
            Logger.logIn(testCreator.getUsername(), testCreator.getPassword());

            Post testPost = testCommunity.addPost("TEST", "test post", Logger.getActiveUser().getUserId());
            System.out.println(testCommunity);

            testCommunity.removePost(testPost.getPostId());
            System.out.println(testCommunity);

            // tests adding and removing comments
            Comment firstComment = testPost.addComment("test comment", Logger.getActiveUser().getUserId());
            Comment secondComment = testPost.addComment("test comment 2", Logger.getActiveUser().getUserId());
            System.out.println(testCommunity);

            testPost.removeComment(firstComment.getCommentId());
            System.out.println(testCommunity);

            // tests removing comments when comment id does not exist in comments list
            try {
                testPost.removeComment(3);
            } catch (IllegalArgumentException e) {
                System.out.println("\nException caught: " + e.getMessage());
            }

            Logger.logOut(testCreator.getUsername());
            System.out.println("\nActive user: " + Logger.getActiveUser());
        } catch (IllegalArgumentException e) {
            System.out.println("\nException caught: " + e.getMessage());
        }
    }
}
