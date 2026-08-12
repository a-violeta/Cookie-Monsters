package com.app.console.communitycommand;

import com.app.console.core.Command;
import com.app.console.core.ConsolePrinter;
import com.app.console.core.ConsoleReader;
import com.app.model.Community;
import com.app.service.CommunityUseCases;

public class FindCommunityCommand extends Command {

    private final CommunityUseCases communityUseCases;
    private final ConsoleReader consoleReader;

    public FindCommunityCommand(ConsolePrinter consolePrinter, CommunityUseCases communityUseCases, ConsoleReader consoleReader) {
        super(consolePrinter);
        this.communityUseCases=communityUseCases;
        this.consoleReader = consoleReader;
    }

    @Override
    public void execute(String[] args) {

        if (args.length >= 1) {
            consolePrinter.printError("Too Many Arguments");
            return;
        }

        try {
            consolePrinter.printPrompt("Type community name");

            // read with console
            String communityName = consoleReader.readLine();

            Community community = communityUseCases.findCommunityByName(communityName);
            consolePrinter.displayCommunity(community);
        } catch (Exception e) {
            consolePrinter.printError(e.getMessage());
        }
    }
}