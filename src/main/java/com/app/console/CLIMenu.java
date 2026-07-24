package com.app.console;

import com.app.model.Community;
import com.app.model.Post;
import com.app.model.User;
import com.app.service.CommentService;
import com.app.service.CommunityService;
import com.app.service.PostService;
import com.app.service.UserUseCases;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CLIMenu implements CommandLineRunner {

    private final CommunityService communityService;
    private final CommentService commentService;
    private final PostService postService;
    private final UserUseCases userUseCases;

    public CLIMenu(CommunityService communityService, CommentService commentService, PostService postService, UserUseCases userUseCases){
        this.communityService = communityService;
        this.commentService = commentService;
        this.postService = postService;
        this.userUseCases = userUseCases;
    }

    @Override
    public void run(String... args){
        ConsoleReader consoleReader = new ConsoleReader();
        ConsolePrinter consolePrinter = new ConsolePrinter();

        try {
            User user1 = userUseCases.createUser("Ion", "ion@test.com", "ion123", "some guy");
            User user2 = userUseCases.createUser("Anca", "anca@test.com", "anca123", "some girl");
            User user3 = userUseCases.createUser("Petru", "petru@test.com", "petru123", "guitarist");
            User user4 = userUseCases.createUser("Adela", "adela@test.com", "adela123", "physicist or smt");

            Community community1 = communityService.createCommunity("The cat lovers", "we really love cats");
            Community community2 = communityService.createCommunity("Anca s community", "Anca is here");

            communityService.joinCommunity(community1.getId(), user1.getUserId());
            communityService.joinCommunity(community1.getId(), user2.getUserId());
            communityService.joinCommunity(community1.getId(), user3.getUserId());

            communityService.joinCommunity(community2.getId(), user2.getUserId());

            Post post1 = postService.addPost(community1.getId(), user1.getUserId(), "First post about cats", "Cats are awesome");
            Post post2 = postService.addPost(community1.getId(), user2.getUserId(), "Second post abouts cats", "Cats are still awesome");

            commentService.addComment("So true", user2.getUserId(), post1.getId());
            commentService.addComment("Yesss", user3.getUserId(), post1.getId());

        } catch (Exception e) {
        }

        consolePrinter.printBanner();

        while (true) {
            boolean isAuthenticated = false;

            while (!isAuthenticated) {
                System.out.println("\n=================================");
                System.out.println("       1. Login                  ");
                System.out.println("       2. Create Account         ");
                System.out.println("       0. Exit                   ");
                System.out.println("=================================");
                System.out.print("Choose option: ");

                String option = consoleReader.readLine();

                switch (option) {
                    case "1":
                        System.out.print("Username or Email: ");
                        String loginIdentifier = consoleReader.readLine();
                        System.out.print("Password: ");
                        String loginPass = consoleReader.readLine();
                        try {
                            userUseCases.login(loginIdentifier, loginPass);
                            consolePrinter.printSuccess("Welcome back, " + userUseCases.getLoggedInUser().getUsername() + "!");
                            isAuthenticated = true;
                        } catch (IllegalArgumentException e) {
                            consolePrinter.printError(e.getMessage());
                        }
                        break;
                    case "2":
                        System.out.print("Choose a Username: ");
                        String newUser = consoleReader.readLine();
                        System.out.print("Enter your Email: ");
                        String newEmail = consoleReader.readLine();
                        System.out.print("Choose a Password: ");
                        String newPass = consoleReader.readLine();
                        System.out.print("Short Description: ");
                        String newDesc = consoleReader.readLine();
                        try {
                            userUseCases.createUser(newUser, newEmail, newPass, newDesc);
                            consolePrinter.printSuccess("Account created successfully! You can now log in (Option 1).");
                        } catch (IllegalArgumentException e) {
                            consolePrinter.printError(e.getMessage());
                        }
                        break;
                    case "0":
                        consolePrinter.printGoodbye();
                        System.exit(0);
                        break;
                    default:
                        consolePrinter.printError("Invalid option. Please choose 1, 2, or 0.");
                }
            }

            //after login
            InputParser inputParser = new InputParser(consoleReader, consolePrinter, communityService, commentService, postService, userUseCases);

            inputParser.startListening();
        }
    }
}
