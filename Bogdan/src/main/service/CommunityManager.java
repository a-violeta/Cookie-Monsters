package main.service;

import main.model.Community;
import main.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CommunityManager {
    private static List<Community> communities = new ArrayList<>();

    private CommunityManager() {}

    public static List<Community> getCommunities() {
        return communities;
    }

    public static Community createCommunity(String communityName, String description) {
        for (Community community : communities) {
            if (Objects.equals(communityName, community.getCommunityName())) {
                throw new IllegalArgumentException("Community name is already taken");
            }
        }

        User creator = Logger.getActiveUser();

        if (creator == null) {
            throw new IllegalArgumentException("You must be logged in to create a community");
        }

        Community newCommunity = new Community(communityName, description, creator);
        communities.add(newCommunity);

        return newCommunity;
    }

    public static Community getCommunityByName(String communityName) {
        for (Community community : communities) {
            if (Objects.equals(communityName, community.getCommunityName())) {
                return community;
            }
        }

        return null;
    }
}
