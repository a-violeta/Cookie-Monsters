package com.app.console;

import com.app.model.Community;
import com.app.service.CommunityUseCases;

public class CreateCommunityCommand extends Command {

    private final CommunityUseCases communityUseCases;

    public CreateCommunityCommand(ConsolePrinter consolePrinter, CommunityUseCases communityUseCases) {
        super(consolePrinter);
        this.communityUseCases=communityUseCases;
    }

    @Override
    public void execute(String[] args) {

        // Arguments Validations
        if (args.length < 4) {

            consolePrinter.printError("Missing Arguments");
            consolePrinter.printExplanation("create-community 'Community Name' 'Community Display Name' 'Description' 'Icon URL'");
            return;

        } else if (args.length > 4) {

            consolePrinter.printError("Too Many Arguments");
            consolePrinter.printExplanation("create-community 'Community Name' 'Community Display Name' 'Description' 'Icon URL'");
            return;
        }

        String name = args[0];
        String displayName = args[1];
        String description = args[2];
        String iconUrl = args[3];
        Community newCommunity = communityUseCases.createCommunity(name, displayName, description, iconUrl);

        consolePrinter.printSuccess("Community successfully created!");
        consolePrinter.displayCommunity(newCommunity);
    }
}
