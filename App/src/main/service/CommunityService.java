package main.service;

// singleton class, there is no need for more than 1 entity that manages communities

import main.model.Community;
import main.model.User;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CommunityService {
    private List<Community> applicationCommunities;
    // list of all the communities

    //private constructor for singleton design pattern
    private CommunityService() {
        this.applicationCommunities = new ArrayList<>();
    }

    // holder implementation for singleton
    // so we call method getInstance() once and it spawns 1 CommunityManager
    // if we call the method again it returns the same CommunityManager made earlier
    private static class Holder {
        private static final CommunityService INSTANCE = new CommunityService();
    }

    public static CommunityService getInstance() {
        return CommunityService.Holder.INSTANCE;
    }

    public List<Community> getApplicationCommunities() {
        return applicationCommunities;
    }

    public boolean validateCommunity(Community community){
        if(community.getCommunityId()==0
                || community.getCommunityName().isEmpty()
                || community.getDescription().isEmpty()
                || community.getCommunityUsers().isEmpty()) {
            // validate id, name, description and number of users (there should be at least 1, the creator)
            System.out.println("Community not valid. Check id, name and description!");
            return false;
        }
        return true;
    }

    public void addCommunity(Community community){
        if(validateCommunity(community)){

            for(Community c: applicationCommunities){
                if(c.getCommunityName().equalsIgnoreCase(community.getCommunityName())){
                    System.out.println("Community name already exists!");
                    return;
                }
            }

            applicationCommunities.add(community);
        }
    }

    public void removeCommunity(long communityId){
        Iterator<Community> it = applicationCommunities.iterator();
        // removing from list by using iterator
        while(it.hasNext()){
            Community c = it.next();
            if(c.getCommunityId() == communityId){
                it.remove();
                break;
            }
        }
    }

    public Community findCommunity(long communityId){
        for(Community c : applicationCommunities){
            if(c.getCommunityId() == communityId){
                return c;
            }
        }
        // community not found, then return null
        return null;
    }

    public void listCommunities(){

        if(applicationCommunities.isEmpty()){
            System.out.println("No communities to list!");
            return;
        }
        for(Community c: applicationCommunities){
            System.out.println(c);
        }
    }

    public void joinCommunity(Community community, User user){

        // right now, join means immediate approval into the community since we don't have admins or moderators yet

        community.addUser(user);
    }

    public void exitCommunity(Community community, User user){

        // exiting doesn't need approval
        // if the community has only one user then delete the community

        // check that the person is part of the community
        if(community.findUserById(user.getUserId())!=null){

            if(community.getCommunityUsers().size()==1){
                System.out.println("Community " + community.getCommunityName() + " will be empty if you exit!");
                System.out.println("Choose to delete the community instead!");
            }
            else{

                // exit means removing person from community s communityUsers list
                community.removeUser(user.getUserId());
            }
        }
    }

    // edit community stuff

}
