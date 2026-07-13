package consoleApp;

import consoleApp.service.UserService;
import consoleApp.service.CommunityService;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        UserService accountManager = UserService.getInstance();
        // Logger logger = Logger.getInstance();
        // Logger should also be a singleton class
        // potential singleton class PostManager: manages a list of all posts, edits them, deletes them
        // potential singleton class CommentManager: manages a list of all comments, edits them, deletes them

        CommunityService communityManager = CommunityService.getInstance();

        while(true){
            System.out.println("1. Create account");
            System.out.println("2. Log into account");
            System.out.println("3. Log out of account");
            System.out.println("4. Post (just text)");
            System.out.println("5. Comment");
            System.out.println("6. Create community");
            System.out.println("7. List users");
            System.out.println("8. List posts");
            System.out.println("9. List comments");
            System.out.println("10. List communities");
            System.out.println("0. Exit\n");

            System.out.print("Choose option: ");

            int option = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (option) {

                case 1 -> {
                    // read username, password, description
                    // put a user in applicationUsers list from accountManager
                    System.out.println("--Creating account--");
                    System.out.println("\nType a username: ");
                    String username = scanner.nextLine();
                    System.out.println("\nType a password: ");
                    String password = scanner.nextLine();
                    System.out.println("\nType a description: ");
                    String description = scanner.nextLine();
                    accountManager.createAccount(username, password, description);
                }

                case 2 -> {
                    System.out.println("--Logging into account--");
                    System.out.println("\nType the username: ");
                    String username = scanner.nextLine();
                    System.out.println("\nType the password: ");
                    String password = scanner.nextLine();
                    accountManager.logIn(username, password);
                }

                case 3 -> {
                    System.out.println("--Logging out of account--");
                    System.out.println("\nType the username:  ");
                    String username = scanner.nextLine();
                    accountManager.logOut(username);
                }

                case 4 -> {
                    System.out.println("--Post--");
                    // choose user, community and create text, title

                    // check if there are ANY users
                    List<User> usersList = accountManager.getApplicationUsers();
                    if(usersList.isEmpty()){
                        System.out.println("\nThere is no user who can post!");
                        break;
                    }
                    System.out.println("\nChoose the user who will be posting: ");
                    // choose from logged users
                    List<User> loggedUsers = accountManager.getLoggedUsers();
                    for (int i = 0; i < loggedUsers.size(); i++) {
                        System.out.println(i+1 + ": " + loggedUsers.get(i).getUsername());
                    }
                    // check the number chosen for validity
                    int chosenUserIndex = scanner.nextInt();
                    if (chosenUserIndex < 1 || chosenUserIndex > loggedUsers.size()) {
                        System.out.println("\nInvalid index!");
                        break;
                    }

                    long postingUserId = loggedUsers.get(chosenUserIndex-1).getUserId();

                    // there s no list of communities yet
                    // so there s no validation here yet
                    System.out.println("\nType community id: ");
                    long communityId = scanner.nextLong();

                    scanner.nextLine(); // consume newline

                    System.out.println("\nType a title: ");
                    String title = scanner.nextLine();

                    System.out.println("\nType body text: ");
                    String text = scanner.nextLine();

                    if(!text.isEmpty() && !title.isEmpty()){
                        new Post(communityId, postingUserId, title, text, null);
                        System.out.println("\nPost made successfully!");
                    }
                    else{
                        System.out.println("\nTitle or text empty!");
                    }
                }

                case 5 -> {
                    System.out.println("--Comment--");

                    // check text, userId and postId

                    // check if there are ANY users
                    List<User> usersList = accountManager.getApplicationUsers();
                    if(usersList.isEmpty()){
                        System.out.println("\nThere is no user who can comment!");
                        break;
                    }

                    System.out.println("\nChoose the user who will be commenting: ");
                    // choose from logged users
                    List<User> loggedUsers = accountManager.getLoggedUsers();
                    for (int i = 0; i < loggedUsers.size(); i++) {
                        System.out.println(i+1 + ": " + loggedUsers.get(i).getUsername());
                    }

                    // check the number chosen for validity
                    int chosenUserIndex = scanner.nextInt();
                    if (chosenUserIndex < 1 || chosenUserIndex > loggedUsers.size()) {
                        System.out.println("\nInvalid index!");
                        break;
                    }

                    long commentingUserId = loggedUsers.get(chosenUserIndex-1).getUserId();

                    // there s no list of posts yet
                    // so there s no validation here yet
                    System.out.println("\nType post id: ");
                    long postId = scanner.nextLong();

                    scanner.nextLine(); // consume newline

                    System.out.println("\nType the text: ");
                    String text = scanner.nextLine();

                    if(!text.isEmpty()){
                        new Comment(text, commentingUserId, postId);
                        System.out.println("\nComment made successfully!");
                    }
                    else{
                        System.out.println("\nText empty!");
                    }
                }

                case 6 -> {
                    System.out.println("--Create community--");

                    // choose user to create the community and create description

                    // check if there are ANY users
                    List<User> usersList = accountManager.getApplicationUsers();
                    if(usersList.isEmpty()){
                        System.out.println("\nThere is no user who can make a community!");
                        break;
                    }

                    System.out.println("\nChoose the user who will be creating the community: ");
                    // choose from logged users
                    List<User> loggedUsers = accountManager.getLoggedUsers();
                    for (int i = 0; i < loggedUsers.size(); i++) {
                        System.out.println(i+1 + ": " + loggedUsers.get(i).getUsername());
                    }

                    // check the number chosen for validity
                    int chosenUserIndex = scanner.nextInt();
                    if (chosenUserIndex < 1 || chosenUserIndex > loggedUsers.size()) {
                        System.out.println("\nInvalid index!");
                        break;
                    }

                    User creatorUser = loggedUsers.get(chosenUserIndex-1);

                    // make the members list, list of just 1 user, the creator
                    List<User> membersList = new ArrayList<>();
                    membersList.add(creatorUser);

                    scanner.nextLine(); // consume newline

                    System.out.println("\nType community name: ");
                    String communityName = scanner.nextLine();

                    System.out.println("\nType community description: ");
                    String description = scanner.nextLine();

                    // make the community and add it to the list
                    Community newCommunity = new Community(communityName, description, membersList, null);
                    communityManager.addCommunity(newCommunity);
                    System.out.println("\nCommunity successfully created!");
                }

                case 7 -> {
                    System.out.println("--List users--\n");
                }

                case 8 -> {
                    System.out.println("--List posts--\n");
                }

                case 9 -> {
                    System.out.println("--List comments--\n");
                }

                case 10 -> {
                    System.out.println("--List communities--");
                    System.out.println();

                    communityManager.listCommunities();
                }

                case 0 -> {
                    System.out.println("Goodbye!");
                    return;
                }

                default -> System.out.println("Invalid option.");
            }
        }
    }
}