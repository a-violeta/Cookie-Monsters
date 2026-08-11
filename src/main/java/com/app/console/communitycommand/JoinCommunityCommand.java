package com.app.console.communitycommand;

import com.app.console.core.Command;
import com.app.console.core.ConsolePrinter;
import com.app.console.core.ConsoleReader;
import com.app.model.Community;
import com.app.service.CommunityUseCases;
import com.app.service.UserUseCases;

import java.util.List;
import java.util.UUID;

public class JoinCommunityCommand extends Command {

    private final CommunityUseCases communityUseCases;
    private final UserUseCases userUseCases;
    private final ConsoleReader consoleReader;

    public JoinCommunityCommand(ConsolePrinter consolePrinter, CommunityUseCases communityUseCases, UserUseCases userUseCases, ConsoleReader consoleReader) {
        super(consolePrinter);
        this.communityUseCases=communityUseCases;
        this.userUseCases = userUseCases;
        this.consoleReader = consoleReader;
    }

    @Override
    public void execute(String[] args) {

        // arg validation
        if (args.length > 0) {

            consolePrinter.printError("Too Many Arguments");
            consolePrinter.printExplanation("join-community");
            return;
        }

        try {
            //Long communityId = Long.parseLong(args[0]);

            List<Community> communities = communityUseCases.listCommunities();

            for (int i = 0; i < communities.size(); i++) {
                consolePrinter.printCommunityListItem(i+1, communities.get(i));
            }

            consolePrinter.printPrompt("Choose a community by typing its index");

            // read with console
            // and check the number chosen for validity
            String input = consoleReader.readLine();

            int chosenIndex;
            try {
                chosenIndex = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("'" + input + "' is not a valid number!");
            }

            if (chosenIndex < 1 || chosenIndex > communities.size()) {
                throw new IllegalArgumentException("Index out of bounds!");
            }

            UUID communityId = communities.get(chosenIndex-1).getId();

            Long userId = userUseCases.getLoggedInUser().getId();

            communityUseCases.joinCommunity(communityId, userId);

            consolePrinter.printSuccess("Successfully joined the community!");
        } catch (Exception e) {
            consolePrinter.printError(e.getMessage());
        }
    }
}