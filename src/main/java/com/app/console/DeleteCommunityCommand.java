package com.app.console;

import com.app.service.CommunityUseCases;

public class DeleteCommunityCommand extends Command{

    private CommunityUseCases communityUseCases;

    public DeleteCommunityCommand(ConsolePrinter consolePrinter, CommunityUseCases communityUseCases) {
        super(consolePrinter);
        this.communityUseCases=communityUseCases;
    }

    @Override
    public void execute(String[] args) {

        if (args.length < 1) {
            consolePrinter.printError("Missing Arguments");
            consolePrinter.printExplanation("delete-community 'Community Name'");
            return;
        }
        else if(args.length > 1) {
            consolePrinter.printError("Too Many Arguments");
            consolePrinter.printExplanation("delete-community 'Community Name'");
            return;
        }

        try {
            //long communityId = Long.parseLong(args[0]);
            Long communityId = communityUseCases.findCommunityByName(args[0].toLowerCase()).getId();
            communityUseCases.deleteCommunity(communityId);

            consolePrinter.printSuccess("Community successfully deleted!");
        } catch (Exception e) {
            consolePrinter.printError(e.getMessage());
        }

    }
}
