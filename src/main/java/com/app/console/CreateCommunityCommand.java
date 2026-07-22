package com.app.console;

import com.app.model.Community;
import com.app.service.CommunityUseCases;

public class CreateCommunityCommand extends Command {

    private CommunityUseCases communityUseCases;

    public CreateCommunityCommand(CommunityUseCases communityUseCases) {
        super();
        this.communityUseCases=communityUseCases;
    }

    @Override
    public void execute(String[] args) {

        // Arguments Validations
        if (args.length < 2) {

            System.out.println("Error : Missing Arguments");
            System.out.println("Usage : 4 'Community Name' 'Description' ");
            return;

        } else if (args.length > 2) {

            System.out.println("Error : Too Many Arguments");
            System.out.println("Usage : 4 'Community Name' 'Description' ");
            return;
        }

        Community newCommunity = communityUseCases.createCommunity(args[0], args[1]);
        System.out.println("Community successfully created!");
    }
}
