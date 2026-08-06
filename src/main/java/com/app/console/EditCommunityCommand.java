package com.app.console;

import com.app.model.Community;
import com.app.service.CommunityUseCases;
import com.app.service.UserUseCases;

import java.util.List;

public class EditCommunityCommand extends Command {

    private final CommunityUseCases communityUseCases;
    private final ConsoleReader consoleReader;
    private final UserUseCases userUseCases;

    public EditCommunityCommand(ConsolePrinter consolePrinter, CommunityUseCases communityUseCases, ConsoleReader consoleReader, UserUseCases userUseCases) {
        super(consolePrinter);
        this.communityUseCases=communityUseCases;
        this.consoleReader = consoleReader;
        this.userUseCases = userUseCases;
    }

    @Override
    public void execute(String[] args) {

        if (args.length >= 1) {
            consolePrinter.printError("Too Many Arguments");
            return;
        }

        try {
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

            String communityName = communities.get(chosenIndex-1).getName();

            consolePrinter.printPrompt("Type new community display name");

            // read with console
            String newDisplayName = consoleReader.readLine();

            consolePrinter.printPrompt("Type new community icon URL");

            // read with console
            String newIconUrl = consoleReader.readLine();

            consolePrinter.printPrompt("Type new community description");

            // read with console
            String newDescription = consoleReader.readLine();

            communityUseCases.editCommunity(communityName, newDisplayName, newIconUrl, newDescription, userUseCases.getLoggedInUser().getUsername());

            consolePrinter.printSuccess("Community successfully updated!");

            // all of this just to print the community after the edit
            List<Community> communitiesNewList = communityUseCases.listCommunities();
            Community community = communitiesNewList.get(chosenIndex-1);
            consolePrinter.displayCommunity(community);
        } catch (Exception e) {
            consolePrinter.printError(e.getMessage());
        }
    }
}
