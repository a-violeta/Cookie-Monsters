package com.app.console.core;

import com.app.console.postcommand.PostsFeedCommand;
import com.app.model.User;
import com.app.service.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;


@Component
@Profile("console")
public class CLIMenu implements CommandLineRunner {

    private final CommunityAbstract communityAbstract;
    private final CommentAbstract commentAbstract;
    private final PostAbstract postAbstract;
    private final UserAbstract userAbstract;

    public CLIMenu(CommunityAbstract communityAbstract, CommentAbstract commentAbstract, PostAbstract postAbstract, UserAbstract userAbstract){
        this.communityAbstract = communityAbstract;
        this.commentAbstract = commentAbstract;
        this.postAbstract = postAbstract;
        this.userAbstract = userAbstract;
    }

    @Override
    public void run(String... args){

        ConsoleReader consoleReader = new ConsoleReader();
        ConsolePrinter consolePrinter = new ConsolePrinter();

        try {
            new SeedData(userAbstract, communityAbstract, postAbstract, commentAbstract).seed();
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
                            userAbstract.login(loginIdentifier, loginPass);
                            consolePrinter.printSuccess("Welcome back, " + userAbstract.getLoggedInUser().getUsername() + "!");
                            Command feedPosts = new PostsFeedCommand(consolePrinter, postAbstract, userAbstract);
                            feedPosts.execute(new String[0]);
                            //consolePrinter.printPostLoginHint();
                            isAuthenticated = true;
                        } catch (Exception e) {
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
                            User user = userAbstract.createUser(newUser, newEmail, newPass, newDesc);
                            consolePrinter.printSuccess("Account created successfully! You can now log in (Option 1).");
                            consolePrinter.displayUser(user);
                        } catch (Exception e) {
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
            InputParser inputParser = new InputParser(consoleReader, consolePrinter, communityAbstract, commentAbstract, postAbstract, userAbstract);

            inputParser.startListening();
        }
    }
}