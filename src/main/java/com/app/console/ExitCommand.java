package com.app.console;

import com.app.service.CommunityService;

public class ExitCommand extends Command {

    public ExitCommand(CommunityService communityService) {
        super(communityService);
    }

    @Override
    public void execute(String[] args) {
        System.out.println("Goodbye see you soon !");
        System.exit(0);
    }
}
