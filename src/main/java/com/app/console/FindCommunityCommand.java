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
        // 19 communityid

        if (args.length < 1) {
            consolePrinter.printError("Missing Arguments");
            consolePrinter.printExplanation("19 'Community Id' ");
            return;
        } else if (args.length > 1) {
            consolePrinter.printError("Too Many Arguments");
            consolePrinter.printExplanation("19 'Community Id' ");
            return;
        }

        try {
            Long communityId = Long.parseLong(args[0]);
            Community community = communityUseCases.findCommunityById(communityId);
            consolePrinter.displayCommunity(community);
        } catch (NumberFormatException e) {
            consolePrinter.printError("Community Id must be a number");
        } catch (Exception e) {
            consolePrinter.printError(e.getMessage());
        }
    }
}
