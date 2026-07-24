package com.app.console;

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
            new SeedData(userUseCases, communityService, postService, commentService).seed();
        } catch (Exception e) {
            System.err.println("Failed to seed data: " + e.getMessage());
            e.printStackTrace();
        }

        consolePrinter.printBanner();

        while (true) {
            boolean isAuthenticated = false;

            while (!isAuthenticated) {
                consolePrinter.printMainMenu();
                consolePrinter.printPrompt("Choose option");

                String option = consoleReader.readLine();

                switch (option) {
                    case "1":
                        consolePrinter.printPrompt("Username or Email");
                        String loginIdentifier = consoleReader.readLine();
                        consolePrinter.printPrompt("Password");
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
                        consolePrinter.printPrompt("Choose a Username");
                        String newUser = consoleReader.readLine();
                        consolePrinter.printPrompt("Enter your Email");
                        String newEmail = consoleReader.readLine();
                        consolePrinter.printPrompt("Choose a Password");
                        String newPass = consoleReader.readLine();
                        consolePrinter.printPrompt("Short Description");
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