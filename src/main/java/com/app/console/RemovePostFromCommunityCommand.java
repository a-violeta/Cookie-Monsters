package com.app.console;

import com.app.service.CommunityUseCases;

public class RemovePostFromCommunityCommand extends Command {

    private CommunityUseCases communityUseCases;

    public RemovePostFromCommunityCommand(ConsolePrinter consolePrinter, CommunityUseCases communityUseCases) {
        super(consolePrinter);
        this.communityUseCases=communityUseCases;
    }

    @Override
    public void execute(String[] args) {

        // Arguments Validations
        if (args.length < 2) {

            System.out.println("Error : Missing Arguments");
            System.out.println("Usage : 15 'Community Id' 'Post Id' ");
            return;

        } else if (args.length > 2) {

            System.out.println("Error : Too Many Arguments");
            System.out.println("Usage : 15 'Community Id' 'Post Id' ");
            return;
        }

        try {
            Long communityId = Long.parseLong(args[0]);
            Long postId = Long.parseLong(args[1]);

            communityUseCases.removePostFromCommunity(communityId, postId);
            System.out.println("Post successfully removed from community!");
        } catch (NumberFormatException e) {
            System.out.println("Error : Community Id and Post Id must be numbers");
        }
    }
}
