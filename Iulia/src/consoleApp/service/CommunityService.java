package consoleApp;

// singleton class, there is no need for more than 1 entity that manages communities

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CommunityManager {
    private List<Community> applicationCommunities;
    // list of all the communitites

    //private constructor for singleton design pattern
    private CommunityManager() {
        this.applicationCommunities = new ArrayList<>();
    }

    // holder implementation for singleton
    // so we call method getInstance() once and it spawns 1 CommunityManager
    // if we call the method again it returns the same CommunityManager made earlier
    private static class Holder {
        private static final CommunityManager INSTANCE = new CommunityManager();
    }

    public static CommunityManager getInstance() {
        return CommunityManager.Holder.INSTANCE;
    }

    public List<Community> getApplicationCommunities() {
        return applicationCommunities;
    }

    public void addCommunity(Community community){
        if(community.getCommunityId()!=0
            && !community.getCommunityName().isEmpty()
            && !community.getDescription().isEmpty()
            && !community.getCommunityUsers().isEmpty()){
            // validate id, name, description and number of users (there should be at least 1, the creator)

            applicationCommunities.add(community);
        }
        else {
            System.out.println("Id, name, description or user list invalid!");
            System.out.println(community.getCommunityId());
            System.out.println(community.getCommunityName());
            System.out.println(community.getDescription());
            System.out.println(community.getCommunityUsers().isEmpty());
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
}
