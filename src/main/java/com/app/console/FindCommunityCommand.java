package com.app.console;

import com.app.model.Community;
import com.app.service.CommunityUseCases;

public class FindCommunityCommand extends Command {

    private CommunityUseCases communityUseCases;

    public FindCommunityCommand(ConsolePrinter consolePrinter, CommunityUseCases communityUseCases) {
        super(consolePrinter);
        this.communityUseCases=communityUseCases;
    }

    @Override
    public void execute(String[] args) {

        if (args.length < 1) {
            consolePrinter.printError("Missing Arguments");
            consolePrinter.printExplanation("19 'Community Name' ");
            return;
        } else if (args.length > 1) {
            consolePrinter.printError("Too Many Arguments");
            consolePrinter.printExplanation("19 'Community Name' ");
            return;
        }

        try {
            String communityName = args[0];
            Community community = communityUseCases.findCommunityByName(communityName);
            consolePrinter.displayCommunity(community);
        } catch (Exception e) {
            consolePrinter.printError(e.getMessage());
        }
    }
}
