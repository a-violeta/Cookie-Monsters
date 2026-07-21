package com.app.console;

import com.app.model.Community;
import com.app.service.CommunityService;

import java.util.List;

public class ListCommunityCommand extends Command{

    public ListCommunityCommand(CommunityService communityService) {
        super(communityService);
    }

    @Override
    public void execute(String[] args) {

        List<Community> communities = communityService.listCommunities();

        for(Community c: communities){
            System.out.println(c);
        }
    }
}
