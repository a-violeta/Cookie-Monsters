package com.app.console;

import com.app.service.CommunityService;

public class ListCommunityCommand extends Command{

    public ListCommunityCommand(CommunityService communityService) {
        super(communityService);
    }

    @Override
    public void execute(String[] args) {

        communityService.listCommunities();
    }
}
