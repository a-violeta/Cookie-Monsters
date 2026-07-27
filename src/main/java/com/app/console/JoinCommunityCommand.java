package com.app.console;

import com.app.model.Community;
import com.app.service.CommunityUseCases;
import com.app.service.UserUseCases;

import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class JoinCommunityCommand extends Command {

    private CommunityUseCases communityUseCases;
    private UserUseCases userUseCases;

    public JoinCommunityCommand(ConsolePrinter consolePrinter, CommunityUseCases communityUseCases, UserUseCases userUseCases) {
        super(consolePrinter);
        this.communityUseCases=communityUseCases;
        this.userUseCases = userUseCases;
    }

    @Override
    public void execute(String[] args) {
        // 17 communityid

        // Arguments Validations
        /*
        if (args.length < 2) {

            consolePrinter.printError("Missing Arguments");
            consolePrinter.printExplanation("join-community 'Community Id' 'User Id'");
            return;

        } else if (args.length > 2) {

            consolePrinter.printError("Too Many Arguments");
            consolePrinter.printExplanation("join-community 'Community Id' 'User Id'");
            return;
        }
         */

        try {
            //Long communityId = Long.parseLong(args[0]);

            List<Community> communities = communityUseCases.listCommunities();

            for (int i = 0; i < communities.size(); i++) {
                System.out.println(i+1 + ": " + communities.get(i));
            }

            System.out.println("\nChoose a community by typing its index: ");

            // check the number chosen for validity
            Scanner scanner = new Scanner(System.in);
            int chosenUserIndex = scanner.nextInt();
            if (chosenUserIndex < 1 || chosenUserIndex > communities.size()) {
                throw new IllegalArgumentException("Index out of bounds!");
            }

            Long communityId = communities.get(chosenUserIndex-1).getId();

            Long userId = userUseCases.getLoggedInUser().getId();

            communityUseCases.joinCommunity(communityId, userId);

            consolePrinter.printSuccess("Successfully joined the community!");
        } catch (Exception e) {
            consolePrinter.printError(e.getMessage());
        }
    }
}
