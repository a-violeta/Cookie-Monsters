package com.app.console;

import com.app.service.CommunityUseCases;

public class ExitCommunityCommand extends Command {

    private CommunityUseCases communityUseCases;

    public ExitCommunityCommand(ConsolePrinter consolePrinter, CommunityUseCases communityUseCases) {
        super(consolePrinter);
        this.communityUseCases=communityUseCases;
    }

    @Override
    public void execute(String[] args) {
        // option 16, arguments: communityid, userid

        // Arguments Validations
        if (args.length < 2) {

            consolePrinter.printError("Missing Arguments");
            consolePrinter.printExplanation("exit-community 'Community Id' 'User Id' ");
            return;

        } else if (args.length > 2) {

            consolePrinter.printError("Too Many Arguments");
            consolePrinter.printExplanation("exit-community 'Community Id' 'User Id' ");
            return;
        }

        try {
            Long communityId = Long.parseLong(args[0]);
            Long userId = Long.parseLong(args[1]);

            communityUseCases.exitCommunity(communityId, userId);
            consolePrinter.printSuccess("Successfully exited the community!");
        } catch (NumberFormatException e) {
            consolePrinter.printError("Community Id and User Id must be numbers");
        } catch (IllegalArgumentException | IllegalStateException e) {
            consolePrinter.printError(e.getMessage());
        }

    }
}
