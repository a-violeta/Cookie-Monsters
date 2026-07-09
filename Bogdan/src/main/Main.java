package main;

import main.model.Comment;
import main.model.Community;
import main.model.Post;
import main.model.User;
import main.service.AccountManager;
import main.service.CommunityManager;
import main.service.Logger;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== H4XXOR2 Reddit ===");

        while (true) {
            try {
                printMenu();
                System.out.print("\nSelect an option: ");
                int choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1 -> {
                        System.out.print("Username: ");
                        String username = scanner.nextLine();
                        System.out.print("Password: ");
                        String password = scanner.nextLine();
                        System.out.print("Description: ");
                        String desc = scanner.nextLine();

                        AccountManager.createAccount(username, password, desc);
                        System.out.println("Account created successfully.");
                    }

                    case 2 -> {
                        System.out.print("Username: ");
                        String loginUsername = scanner.nextLine();
                        System.out.print("Password: ");
                        String loginPassword = scanner.nextLine();

                        Logger.logIn(loginUsername, loginPassword);

                        if (Logger.getActiveUser() != null) {
                            System.out.println("Logged in as " + Logger.getActiveUser().getUsername());
                        } else {
                            System.out.println("Wrong username or password.");
                        }
                    }

                    case 3 -> {
                        if (Logger.getActiveUser() != null) {
                            String activeUser = Logger.getActiveUser().getUsername();
                            Logger.logOut(activeUser);
                            System.out.println("Logged out " + activeUser);
                        } else {
                            System.out.println("You are not logged in.");
                        }
                    }

                    case 4 -> {
                        System.out.print("Community name: ");
                        String communityName = scanner.nextLine();
                        System.out.print("Description: ");
                        String description = scanner.nextLine();

                        CommunityManager.createCommunity(communityName, description);
                        System.out.println("Community created successfully.");
                    }

                    case 5 -> {
                        System.out.print("Community name: ");
                        String communityName = scanner.nextLine();

                        Community community = CommunityManager.getCommunityByName(communityName);

                        if (community != null) {
                            System.out.println(community);
                        } else {
                            System.out.println("Community " + communityName + " not found.");
                        }
                    }

                    case 6 -> {
                        System.out.print("Community name: ");
                        String communityName = scanner.nextLine();

                        Community community = CommunityManager.getCommunityByName(communityName);

                        if (community != null) {
                            System.out.print("Member username: ");
                            String memberUsername = scanner.nextLine();

                            User newMember = AccountManager.getUserByUsername(memberUsername);

                            if (newMember != null) {
                                community.addMember(newMember);
                                System.out.println("Member added successfully.");
                            } else {
                                System.out.println("User " + memberUsername + " not found.");
                            }
                        } else {
                            System.out.println("Community " + communityName + " not found.");
                        }
                    }

                    case 7 -> {
                        System.out.print("Community name: ");
                        String communityName = scanner.nextLine();

                        Community community = CommunityManager.getCommunityByName(communityName);

                        if (community != null) {
                            System.out.print("Title: ");
                            String title = scanner.nextLine();
                            System.out.print("Text: ");
                            String text = scanner.nextLine();

                            User activeUser = Logger.getActiveUser();

                            if (activeUser != null) {
                                Post newPost = community.addPost(title, text, activeUser.getUserId());
                                System.out.println("Post added successfully.");
                            } else {
                                System.out.println("You are not logged in.");
                            }
                        } else {
                            System.out.println("Community " + communityName + " not found.");
                        }
                    }

                    case 8 -> {
                        System.out.print("Community name: ");
                        String communityName = scanner.nextLine();

                        Community community = CommunityManager.getCommunityByName(communityName);

                        if (community != null) {
                            System.out.print("Post id: ");
                            int postId = Integer.parseInt(scanner.nextLine());

                            community.removePost(postId);
                            System.out.println("Post removed successfully.");
                        } else {
                            System.out.println("Community " + communityName + " not found.");
                        }
                    }

                    case 9 -> {
                        System.out.print("Community name: ");
                        String communityName = scanner.nextLine();
                        Community community = CommunityManager.getCommunityByName(communityName);

                        if (community != null) {
                            System.out.print("Post title: ");
                            String title = scanner.nextLine();

                            for (Post post : community.getPosts()) {
                                if (post.getTitle().equalsIgnoreCase(title)) {
                                    User activeUser = Logger.getActiveUser();

                                    if (activeUser != null) {
                                        System.out.print("Comment text: ");
                                        String text = scanner.nextLine();

                                        post.addComment(text, activeUser.getUserId());
                                        System.out.println("Comment added successfully.");
                                    } else {
                                        System.out.println("You are not logged in.");
                                    }
                                }
                            }
                        } else {
                            System.out.println("Community " + communityName + " not found.");
                        }
                    }

                    case 10 -> {
                        System.out.print("Community name: ");
                        String communityName = scanner.nextLine();
                        Community community = CommunityManager.getCommunityByName(communityName);

                        if (community != null) {
                            System.out.print("Post title: ");
                            String title = scanner.nextLine();

                            for (Post post : community.getPosts()) {
                                if (post.getTitle().equalsIgnoreCase(title)) {
                                    User activeUser = Logger.getActiveUser();

                                    if (activeUser != null) {
                                        System.out.print("Comment id: ");
                                        int commentId = Integer.parseInt(scanner.nextLine());

                                        post.removeComment(commentId);
                                        System.out.println("Comment removed successfully.");
                                    } else {
                                        System.out.println("You are not logged in.");
                                    }
                                }
                            }
                        } else {
                            System.out.println("Community " + communityName + " not found.");
                        }
                    }

                    case 0 -> {
                        System.out.println("Goodbye!");
                        return;
                    }

                    default -> System.out.println("Invalid option.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private static void printMenu() {
        System.out.println("\nActive user: " +
                (Logger.getActiveUser() != null ? Logger.getActiveUser().getUsername() : "None"));
        System.out.println("\n1. Create account");
        System.out.println("2. Login");
        System.out.println("3. Logout");
        System.out.println("4. Create community");
        System.out.println("5. Display community");
        System.out.println("6. Add member to community");
        System.out.println("7. Add post");
        System.out.println("8. Remove post");
        System.out.println("9. Add comment");
        System.out.println("10. Remove comment");
        System.out.println("0. Exit");
    }
}
