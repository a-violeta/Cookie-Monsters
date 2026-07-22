package com.app.console;

import com.app.model.Community;
import com.app.service.CommunityUseCases;

import java.util.List;

public class ListCommunityCommand extends Command{

    private CommunityUseCases communityUseCases;

    public ListCommunityCommand(CommunityUseCases communityUseCases) {
        super();
        this.communityUseCases=communityUseCases;
    }

    @Override
    public void execute(String[] args) {

        List<Community> communities = communityUseCases.listCommunities();

        for(Community c: communities){
            System.out.println(c);
        }
    }
}
