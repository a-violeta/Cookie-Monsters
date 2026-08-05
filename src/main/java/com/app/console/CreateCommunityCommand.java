package com.app.console;

import com.app.model.Community;
import com.app.service.CommunityUseCases;

public class CreateCommunityCommand extends Command {

    private final CommunityUseCases communityUseCases;
    private final ConsoleReader consoleReader;

    public CreateCommunityCommand(ConsolePrinter consolePrinter, CommunityUseCases communityUseCases, ConsoleReader consoleReader) {
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

        consolePrinter.printPrompt("Type community name");

        // read with console
        String name = consoleReader.readLine();

        consolePrinter.printPrompt("Type community display name");

        // read with console
        String displayName = consoleReader.readLine();

        consolePrinter.printPrompt("Type community description");

        // read with console
        String description = consoleReader.readLine();

        consolePrinter.printPrompt("Type community icon URL");

        // read with console
        String iconUrl = consoleReader.readLine();

        Community newCommunity = communityUseCases.createCommunity(name, displayName, description, iconUrl);

        consolePrinter.printSuccess("Community successfully created!");
        consolePrinter.displayCommunity(newCommunity);
    }
}
