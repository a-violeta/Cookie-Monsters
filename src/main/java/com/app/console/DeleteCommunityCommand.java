package com.app.console;

import com.app.service.CommunityUseCases;

public class DeleteCommunityCommand extends Command{

    private final CommunityUseCases communityUseCases;
    private final ConsoleReader consoleReader;

    public DeleteCommunityCommand(ConsolePrinter consolePrinter, CommunityUseCases communityUseCases, ConsoleReader consoleReader) {
        super(consolePrinter);
        this.communityUseCases=communityUseCases;
        this.consoleReader = consoleReader;
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
            communityUseCases.deleteCommunity(communityName);

            consolePrinter.printSuccess("Community successfully deleted!");
        } catch (Exception e) {
            consolePrinter.printError(e.getMessage());
        }

    }
}
