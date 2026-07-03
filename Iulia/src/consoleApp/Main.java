package consoleApp;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        AccountManager accountManager = AccountManager.getInstance();
        // Logger logger = Logger.getInstance();
        // Logger should also be a singleton class
        // potential singleton class PostManager: manages a list of all posts, edits them, deletes them
        // potential singleton class CommentManager: manages a list of all comments, edits them, deletes them

        while(true){
            System.out.println("1. Create account");
            System.out.println("2. Log into account");
            System.out.println("3. Log out of account");
            System.out.println("4. Post (just text)");
            System.out.println("5. Comment");
            System.out.println("6. Create community");
            System.out.println("0. Exit\n");

            System.out.print("Choose option: ");

            int option = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (option) {

                case 1 -> {
                    // read username, password, description
                    // put a user in applicationUsers list from accountManager
                    System.out.println("\nCreating account\n");
                    System.out.println("\nType a username\n");
                    String username = scanner.nextLine();
                    System.out.println("\nType a password\n");
                    String password = scanner.nextLine();
                    System.out.println("\nType a description\n");
                    String description = scanner.nextLine();
                    //accountManager.createUser(useraname, password, description);
                }
                case 2 -> {
                    System.out.println("\nLogging into account\n");
                    System.out.println("\nType a username\n");
                    String username = scanner.nextLine();
                    System.out.println("\nType the password\n");
                    String password = scanner.nextLine();
                    //logger.logIn(useraname, password);
                }
                case 3 -> {
                    System.out.println("\nLogging out of account\n");
                    System.out.println("\nType a username\n");
                    String username = scanner.nextLine();
                    System.out.println("\nType the password\n");
                    String password = scanner.nextLine();
                    //logger.logOut(useraname, password);
                }
                case 4 -> {
                    System.out.println("\nPost\n");
                    // choose user, community and text
                    List<User> usersList = accountManager.getApplicationUsers();
                    if(usersList.isEmpty()){
                        System.out.println("There is no user who can post");
                        break;
                    }
                    for (int i = 0; i < usersList.size(); i++) {
                        System.out.println(i+1 + ": " + usersList.get(i).getUsername());
                    }
                    int chosenUser = scanner.nextInt();
                    if (chosenUser < 1 || chosenUser > usersList.size()) {
                        System.out.println("Invalid index!");
                        break;
                    }

                }
                case 5 -> {
                    System.out.println("\nComment\n");
                }
                case 6 -> {
                    System.out.println("\nCreate community\n");
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