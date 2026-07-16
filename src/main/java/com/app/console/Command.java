package com.app.console;

import com.app.service.CommunityService;

public abstract class Command {

    //
    protected final CommunityService communityService;

    public Command(CommunityService communityService) {
        this.communityService = communityService;
    }

    public abstract void execute(String[] args);

}
