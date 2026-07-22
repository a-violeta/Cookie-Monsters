package com.app.console;

import com.app.service.CommunityUseCases;

public class EditCommunityCommand extends Command {

    private CommunityUseCases communityUseCases;

    public EditCommunityCommand(CommunityUseCases communityUseCases) {
        super();
        this.communityUseCases=communityUseCases;
    }

    @Override
    public void execute(String[] args) {
        // 18 communityid description

        if (args.length < 2) {
            System.out.println("Error : Missing Arguments");
            System.out.println("Usage : 18 'Community Id' 'New Description' ");
            return;
        } else if (args.length > 2) {
            System.out.println("Error : Too Many Arguments");
            System.out.println("Usage : 18 'Community Id' 'New Description' ");
            return;
        }

        try {
            Long communityId = Long.parseLong(args[0]);
            communityUseCases.editCommunity(communityId, args[1]);
            System.out.println("Community successfully updated!");
        } catch (NumberFormatException e) {
            System.out.println("Error : Community Id must be a number");
        } catch (IllegalArgumentException e) {
            System.out.println("Error : " + e.getMessage());
        }
    }
}
