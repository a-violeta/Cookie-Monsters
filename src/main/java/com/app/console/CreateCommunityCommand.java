package com.app.console;

import com.app.model.Community;
import com.app.service.CommunityUseCases;

public class CreateCommunityCommand extends Command {

    private CommunityUseCases communityUseCases;

    public CreateCommunityCommand(ConsolePrinter consolePrinter, CommunityUseCases communityUseCases) {
        super(consolePrinter);
        this.communityUseCases=communityUseCases;
    }

    @Override
    public void execute(String[] args) {

        // Arguments Validations
        if (args.length < 2) {

            consolePrinter.printError("Missing Arguments");
            consolePrinter.printExplanation("4 'Community Name' 'Description'");
            return;

        } else if (args.length > 2) {

            consolePrinter.printError("Too Many Arguments");
            consolePrinter.printExplanation("4 'Community Name' 'Description'");
            return;
        }

        Community newCommunity = communityUseCases.createCommunity(args[0], args[1]);

        consolePrinter.printSuccess("Community successfully created!");
        consolePrinter.displayCommunity(newCommunity);
    }
}
