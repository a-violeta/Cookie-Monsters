package com.app.console;

import com.app.service.CommunityUseCases;

public class EditCommunityCommand extends Command {

    private CommunityUseCases communityUseCases;

    public EditCommunityCommand(ConsolePrinter consolePrinter, CommunityUseCases communityUseCases) {
        super(consolePrinter);
        this.communityUseCases=communityUseCases;
    }

    @Override
    public void execute(String[] args) {
        // 18 communityid description

        if (args.length < 2) {
            consolePrinter.printError("Missing Arguments");
            consolePrinter.printExplanation("edit-community 'Community Id' 'New Description' ");
            return;
        } else if (args.length > 2) {
            consolePrinter.printError("Too Many Arguments");
            consolePrinter.printExplanation("edit-community 'Community Id' 'New Description' ");
            return;
        }

        try {
            Long communityId = Long.parseLong(args[0]);
            communityUseCases.editCommunity(communityId, args[1]);
            consolePrinter.printSuccess("Community successfully updated!");
        } catch (NumberFormatException e) {
            consolePrinter.printError("Community Id must be a number");
        } catch (Exception e) {
            consolePrinter.printError(e.getMessage());
        }
    }
}
