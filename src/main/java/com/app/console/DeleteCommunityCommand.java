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
            System.out.println("Error : Missing Arguments");
            System.out.println("Usage : 14 \"Community ID\"");
            return;
        }

        try {
            long communityId = Long.parseLong(args[0]);
            communityUseCases.deleteCommunity(communityId);

            consolePrinter.printSuccess("Community successfully deleted!");
        } catch (NumberFormatException e) {
            System.out.println("Error : Community Id must be a number");
        } catch (Exception e) {
            System.out.println("Error : " + e.getMessage());
        }

    }
}
