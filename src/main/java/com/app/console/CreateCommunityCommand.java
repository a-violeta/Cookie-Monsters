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


        User user1 = new User("Ion", "ion123", "some guy");

        List<User> membersList = new ArrayList<>();


        membersList.add(user1);

        Community newCommunity = new Community(args[0], args[1], membersList, null);

        communityService.addCommunity(newCommunity);

        System.out.println("\nCommunity successfully created!");
    }
}
