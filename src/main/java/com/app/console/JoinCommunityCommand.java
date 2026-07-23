package com.app.console;

import com.app.service.CommunityUseCases;

public class JoinCommunityCommand extends Command {

    private CommunityUseCases communityUseCases;

    public JoinCommunityCommand(ConsolePrinter consolePrinter, CommunityUseCases communityUseCases) {
        super(consolePrinter);
        this.communityUseCases=communityUseCases;
    }

    @Override
    public void execute(String[] args) {
        // 17 communityid, userid

        // Arguments Validations
        if (args.length < 2) {

            consolePrinter.printError("Missing Arguments");
            consolePrinter.printExplanation("17 'Community Id' 'User Id'");
            return;

        } else if (args.length > 2) {

            consolePrinter.printError("Too Many Arguments");
            consolePrinter.printExplanation("17 'Community Id' 'User Id'");
            return;
        }

        try {
            Long communityId = Long.parseLong(args[0]);
            Long userId = Long.parseLong(args[1]);

            communityUseCases.joinCommunity(communityId, userId);

            consolePrinter.printSuccess("Successfully joined the community!");
        } catch (NumberFormatException e) {
            consolePrinter.printError("Community Id and User Id must be numbers");
        } catch (Exception e) {
            consolePrinter.printError(e.getMessage());
        }
    }
}
