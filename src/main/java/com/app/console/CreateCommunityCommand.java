package com.app.console;

import com.app.model.Community;
import com.app.model.User;
import com.app.service.CommunityService;

import java.util.ArrayList;
import java.util.List;



public class CreateCommunityCommand extends Command {

    public CreateCommunityCommand(CommunityService communityService) {
        super(communityService);
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

        User user1 = new User("Ion", "ion123", "some guy");

        List<User> membersList = new ArrayList<>();

        membersList.add(user1);

        Community community = new Community(args[0], args[1], membersList,new ArrayList<>());

        Community newCommunity = communityService.addCommunity(community);
        System.out.println("Community successfully created!");
    }
}
