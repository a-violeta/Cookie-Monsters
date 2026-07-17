package com.app.console;

import com.app.service.CommunityService;

public class DeleteCommunityCommand extends Command{

    public DeleteCommunityCommand(CommunityService communityService) {
        super(communityService);
    }

    @Override
    public void execute(String[] args) {

        if (args.length < 1) {
            System.out.println("Error : Missing Arguments");
            System.out.println("Usage : 14 \"Community ID\"");
            return;
        }

        long communityId = Long.parseLong(args[0]);
        communityService.removeCommunity(communityId);

    }
}
