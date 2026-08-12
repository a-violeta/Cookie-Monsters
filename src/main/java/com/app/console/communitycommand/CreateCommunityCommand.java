package com.app.console.communitycommand;

import com.app.console.core.Command;
import com.app.console.core.ConsolePrinter;
import com.app.console.core.ConsoleReader;
import com.app.model.Community;
import com.app.service.CommunityAbstract;
import com.app.service.UserAbstract;

public class CreateCommunityCommand extends Command {

    private final CommunityAbstract communityAbstract;
    private final ConsoleReader consoleReader;
    private final UserAbstract userAbstract;

    public CreateCommunityCommand(ConsolePrinter consolePrinter, CommunityAbstract communityAbstract, ConsoleReader consoleReader, UserAbstract userAbstract) {
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

        Community newCommunity = communityAbstract.createCommunity(name, displayName, description, iconUrl, userAbstract.getLoggedInUser().getUsername());

        consolePrinter.printSuccess("Community successfully created!");
        consolePrinter.displayCommunity(newCommunity);
    }
}
