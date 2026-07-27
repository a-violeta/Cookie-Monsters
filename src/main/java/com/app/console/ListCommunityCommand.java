package com.app.console;

import com.app.model.Community;
import com.app.service.CommunityUseCases;

import java.util.List;

public class ListCommunityCommand extends Command{

    private CommunityUseCases communityUseCases;

    public ListCommunityCommand(ConsolePrinter consolePrinter, CommunityUseCases communityUseCases) {
        super(consolePrinter);
        this.communityUseCases=communityUseCases;
    }

    @Override
    public void execute(String[] args) {

        // Arguments Validations
        if (args.length > 0) {

            consolePrinter.printError("Too Many Arguments");
            consolePrinter.printExplanation("list-communities");
            return;
        }

        List<Community> communities = communityUseCases.listCommunities();

        if(communities.isEmpty()){
            consolePrinter.printError("No communities found!");
        }
        else{
            for(Community c: communities){
                consolePrinter.displayCommunity(c);
            }
        }
    }
}
