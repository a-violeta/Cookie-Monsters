package com.app.console.communitycommand;

import com.app.console.core.Command;
import com.app.console.core.ConsolePrinter;
import com.app.model.Community;
import com.app.service.CommunityAbstract;

import java.util.List;

public class ListCommunityCommand extends Command {

    private final CommunityAbstract communityAbstract;

    public ListCommunityCommand(ConsolePrinter consolePrinter, CommunityAbstract communityAbstract) {
        super(consolePrinter);
        this.communityAbstract = communityAbstract;
    }

    @Override
    public void execute(String[] args) {

        // arg validation
        if (args.length > 0) {

            consolePrinter.printError("Too Many Arguments");
            consolePrinter.printExplanation("list-communities");
            return;
        }

        List<Community> communities = communityAbstract.listCommunities();

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