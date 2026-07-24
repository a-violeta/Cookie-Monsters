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
            System.out.println("Error : Missing Arguments");
            System.out.println("Usage : 19 'Community Id' ");
            return;
        } else if (args.length > 1) {
            System.out.println("Error : Too Many Arguments");
            System.out.println("Usage : 19 'Community Id' ");
            return;
        }

        try {
            Long communityId = Long.parseLong(args[0]);
            Community community = communityUseCases.findCommunityById(communityId);
            System.out.println(community);
        } catch (NumberFormatException e) {
            System.out.println("Error : Community Id must be a number");
        } catch (IllegalArgumentException e) {
            System.out.println("Error : " + e.getMessage());
        }
    }
}
