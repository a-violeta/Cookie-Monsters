package com.app.console;

import com.app.model.*;
import com.app.service.CommunityService;
import com.app.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
// @Profile("cli")
public class CLIMenu implements CommandLineRunner {

    private final CommunityService communityService;
    private final UserService userService; //declare user

    public CLIMenu(CommunityService communityService, UserService userService){
        this.communityService = communityService;
        this.userService = userService;
    }

    @Override
    public void run(String... args){

        try {
            userService.createUser("Ion", "ion123", "some guy");
            userService.createUser("Anca", "anca123", "some girl");
            userService.createUser("Petru", "petru123", "guitarist");
            userService.createUser("Adela", "adela123", "physicist or smt");
        } catch (IllegalArgumentException e) {
            //ignore if user exists
        }

        ConsoleReader consoleReader = new ConsoleReader();
        User loggedInUser = null;

        System.out.println("Login to cnontinue\n");

        //While until you login using credentials
        while (loggedInUser == null) {
            System.out.print("Username: ");
            String username = consoleReader.readLine();

            System.out.print("Password: ");
            String password = consoleReader.readLine();

            try {
                loggedInUser = userService.login(username, password);
                System.out.println("\nLLogin succesful " + loggedInUser.getUsername() + "!\n");
            } catch (IllegalArgumentException e) {
                //error for wrong credentials
                System.out.println("\nError: " + e.getMessage());
                System.out.println("Try again.\n");
            }
        }

        // access to command after login
        InputParser inputParser = new InputParser(consoleReader, communityService);
        inputParser.startListening();
    }
}