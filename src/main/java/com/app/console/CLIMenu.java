package com.app.console;

import com.app.model.User;
import com.app.service.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
public class CLIMenu implements CommandLineRunner {

    private final CommunityUseCases communityUseCases;
    private final CommentUseCases commentUseCases;
    private final PostUseCases postUseCases;
    private final UserUseCases userUseCases;

    public CLIMenu(CommunityUseCases communityUseCases, CommentUseCases commentUseCases, PostUseCases postUseCases, UserUseCases userUseCases){
        this.communityUseCases = communityUseCases;
        this.commentUseCases = commentUseCases;
        this.postUseCases = postUseCases;
        this.userUseCases = userUseCases;
    }

    @Override
    public void run(String... args){

        ConsoleReader consoleReader = new ConsoleReader();
        ConsolePrinter consolePrinter = new ConsolePrinter();

        try {
            new SeedData(userUseCases, communityUseCases, postUseCases, commentUseCases).seed();
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
                        String loginPass = consoleReader.readSecret();
                        try {
                            userUseCases.login(loginIdentifier, loginPass);
                            consolePrinter.printSuccess("Welcome back, " + userUseCases.getLoggedInUser().getUsername() + "!");
                            Command feedPosts = new PostsFeedCommand(consolePrinter, postUseCases);
                            feedPosts.execute(new String[0]);
                            consolePrinter.printPostLoginHint();
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
                        String newPass = consoleReader.readSecret();
                        consolePrinter.printPrompt("Short Description");
                        String newDesc = consoleReader.readLine();
                        try {
                            User user = userUseCases.createUser(newUser, newEmail, newPass, newDesc);
                            consolePrinter.printSuccess("Account created successfully! You can now log in (Option 1).");
                            consolePrinter.displayUser(user);
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
            InputParser inputParser = new InputParser(consoleReader, consolePrinter, communityUseCases, commentUseCases, postUseCases, userUseCases);

            inputParser.startListening();
        }
    }
}