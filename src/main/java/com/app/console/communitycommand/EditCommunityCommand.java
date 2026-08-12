package com.app.console.communitycommand;

import com.app.console.core.Command;
import com.app.console.core.ConsolePrinter;
import com.app.console.core.ConsoleReader;
import com.app.model.Community;
import com.app.service.CommunityAbstract;
import com.app.service.UserAbstract;

import java.util.List;

public class EditCommunityCommand extends Command {

    private final CommunityAbstract communityAbstract;
    private final ConsoleReader consoleReader;
    private final UserAbstract userAbstract;

    public EditCommunityCommand(ConsolePrinter consolePrinter, CommunityAbstract communityAbstract, ConsoleReader consoleReader, UserAbstract userAbstract) {
        super(consolePrinter);
        this.communityAbstract = communityAbstract;
        this.consoleReader = consoleReader;
        this.userAbstract = userAbstract;
    }

    @Override
    public void execute(String[] args) {

        if (args.length >= 1) {
            consolePrinter.printError("Too Many Arguments");
            return;
        }

        try {
            List<Community> communities = communityAbstract.listCommunities();

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

            communityAbstract.editCommunity(communityName, newDisplayName, newIconUrl, newDescription, userAbstract.getLoggedInUser().getUsername());

            consolePrinter.printSuccess("Community successfully updated!");

            // all of this just to print the community after the edit
            List<Community> communitiesNewList = communityAbstract.listCommunities();
            Community community = communitiesNewList.get(chosenIndex-1);
            consolePrinter.displayCommunity(community);
        } catch (Exception e) {
            consolePrinter.printError(e.getMessage());
        }
    }
}
