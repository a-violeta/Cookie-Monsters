package com.app.console.communitycommand;

import com.app.console.core.Command;
import com.app.console.core.ConsolePrinter;
import com.app.console.core.ConsoleReader;
import com.app.service.CommunityUseCases;
import com.app.service.UserUseCases;

public class DeleteCommunityCommand extends Command {

    private final CommunityUseCases communityUseCases;
    private final ConsoleReader consoleReader;
    private final UserUseCases userUseCases;

    public DeleteCommunityCommand(ConsolePrinter consolePrinter, CommunityUseCases communityUseCases, ConsoleReader consoleReader, UserUseCases userUseCases) {
        super(consolePrinter);
        this.communityUseCases=communityUseCases;
        this.consoleReader = consoleReader;
        this.userUseCases = userUseCases;
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

            String communityName = communityUseCases.findCommunityByName(name.toLowerCase()).getName();
            communityUseCases.deleteCommunity(communityName, userUseCases.getLoggedInUser().getUsername());

            consolePrinter.printSuccess("Community successfully deleted!");
        } catch (Exception e) {
            consolePrinter.printError(e.getMessage());
        }

    }
}
