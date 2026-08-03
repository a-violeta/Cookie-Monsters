package com.app.console;

import com.app.model.Community;
import com.app.service.CommunityUseCases;

import java.util.List;

public class EditCommunityCommand extends Command {

    private final CommunityUseCases communityUseCases;
    private final ConsoleReader consoleReader;

    public EditCommunityCommand(ConsolePrinter consolePrinter, CommunityUseCases communityUseCases, ConsoleReader consoleReader) {
        super(consolePrinter);
        this.communityUseCases=communityUseCases;
        this.consoleReader = consoleReader;
    }

    @Override
    public void execute(String[] args) {

        if (args.length < 2) {
            consolePrinter.printError("Missing Arguments");
            consolePrinter.printExplanation("edit-community 'New Display Name' 'New Description' ");
            return;
        } else if (args.length > 2) {
            consolePrinter.printError("Too Many Arguments");
            consolePrinter.printExplanation("edit-community 'New Display Name' 'New Description' ");
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
            String newDisplayName = args[0];
            String newDescription = args[1];

            communityUseCases.editCommunity(communityName, newDisplayName, newDescription);

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
