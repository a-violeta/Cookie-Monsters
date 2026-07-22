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

            System.out.println("Error : Missing Arguments");
            System.out.println("Usage : 17 'Community Id' 'User Id' ");
            return;

        } else if (args.length > 2) {

            System.out.println("Error : Too Many Arguments");
            System.out.println("Usage : 17 'Community Id' 'User Id' ");
            return;
        }

        try {
            Long communityId = Long.parseLong(args[0]);
            Long userId = Long.parseLong(args[1]);

            communityUseCases.joinCommunity(communityId, userId);
            System.out.println("Successfully joined the community!");
        } catch (NumberFormatException e) {
            System.out.println("Error : Community Id and User Id must be numbers");
        } catch (IllegalArgumentException e) {
            System.out.println("Error : " + e.getMessage());
        }
    }
}
