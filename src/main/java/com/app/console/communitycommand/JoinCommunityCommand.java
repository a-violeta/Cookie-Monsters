package com.app.console.communitycommand;

import com.app.console.core.Command;
import com.app.console.core.ConsolePrinter;
import com.app.console.core.ConsoleReader;
import com.app.model.Community;
import com.app.service.CommunityAbstract;
import com.app.service.UserAbstract;

import java.util.List;
import java.util.UUID;

public class JoinCommunityCommand extends Command {

    private final CommunityAbstract communityAbstract;
    private final UserAbstract userAbstract;
    private final ConsoleReader consoleReader;

    public JoinCommunityCommand(ConsolePrinter consolePrinter, CommunityAbstract communityAbstract, UserAbstract userAbstract, ConsoleReader consoleReader) {
        super(consolePrinter);
        this.communityAbstract = communityAbstract;
        this.userAbstract = userAbstract;
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

            UUID communityId = communities.get(chosenIndex-1).getId();

            Long userId = userAbstract.getLoggedInUser().getId();

            communityAbstract.joinCommunity(communityId, userId);

            consolePrinter.printSuccess("Successfully joined the community!");
        } catch (Exception e) {
            consolePrinter.printError(e.getMessage());
        }
    }
}