package com.app.console;

import com.app.service.CommunityService;
import com.app.service.CommunityUseCases;

public class ListCommunityCommand extends Command{

    private CommunityUseCases communityUseCases;

    public ListCommunityCommand(CommunityUseCases communityUseCases) {
        super();
        this.communityUseCases=communityUseCases;
    }

    @Override
    public void execute(String[] args) {

        communityUseCases.listCommunities();
    }
}
