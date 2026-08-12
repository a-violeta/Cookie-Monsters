package com.app.console.communitycommand;

import com.app.console.core.Command;
import com.app.console.core.ConsolePrinter;
import com.app.console.core.ConsoleReader;
import com.app.service.CommunityAbstract;
import com.app.service.UserAbstract;

public class DeleteCommunityCommand extends Command {

    private final CommunityAbstract communityAbstract;
    private final ConsoleReader consoleReader;
    private final UserAbstract userAbstract;

    public DeleteCommunityCommand(ConsolePrinter consolePrinter, CommunityAbstract communityAbstract, ConsoleReader consoleReader, UserAbstract userAbstract) {
        super(consolePrinter);
        this.communityAbstract = communityAbstract;
        this.consoleReader = consoleReader;
        this.userAbstract = userAbstract;
    }

    @Override
    public void execute(String[] args) {

        if(args.length >= 1) {
            consolePrinter.printError("Too Many Arguments");
            return;
        }

        try {
            consolePrinter.printPrompt("Type community name");

            // read with console
            String name = consoleReader.readLine();

            String communityName = communityAbstract.findCommunityByName(name.toLowerCase()).getName();
            communityAbstract.deleteCommunity(communityName, userAbstract.getLoggedInUser().getUsername());

            consolePrinter.printSuccess("Community successfully deleted!");
        } catch (Exception e) {
            consolePrinter.printError(e.getMessage());
        }

    }
}
