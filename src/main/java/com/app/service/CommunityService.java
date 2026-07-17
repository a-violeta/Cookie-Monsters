package com.app.service;

import com.app.model.Community;
import com.app.model.Post;
import com.app.model.User;
import com.app.repository.H2CommunityRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;


@Service
@RequiredArgsConstructor

public class CommunityService {

    private final H2CommunityRepository h2CommunityRepository;
    private List<Community> applicationCommunities = new ArrayList<>();
    // list of all the communities

    public void validateCommunity(String communityName, String description) {
        if (communityName == null || communityName.isBlank()) {
            throw new IllegalArgumentException("Community name is required");
        }

        if (!communityName.matches("^[a-zA-Z0-9_]+$")) {
            throw new IllegalArgumentException("Community name must contain only letters, numbers, and '_'");
        }

        if (communityName.length() < 3) {
            throw new IllegalArgumentException("Community name must have at least 3 characters");
        }

        if (communityName.length() > 21) {
            throw new IllegalArgumentException("Community name is too long");
        }

        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Community description is required");
        }

        for (Community community : applicationCommunities) {
            if (Objects.equals(communityName, community.getCommunityName())) {
                throw new IllegalArgumentException("Community name is already taken");
            }
        }
    }

    /*public Community addCommunity(String communityName, String description) {
        validateCommunity(communityName, description);
        Community community = new Community(communityName, description, new ArrayList<>(), new ArrayList<>());
        applicationCommunities.add(community);
        return community;
    }*/

    public Community findCommunityById(long communityId) {
        for (Community community : applicationCommunities) {
            if (community.getId() == communityId) {
                return community;
            }
        }
        // community not found, then return null
        return null;
    }

    public void editCommunity(long communityId, String description) {
        Community community = findCommunityById(communityId);
        if (community != null) {
            validateCommunity(community.getCommunityName(), description);
            community.setDescription(description);
        } else {
            throw new IllegalArgumentException("Community with id " + communityId + " not found");
        }
    }

    public void deleteCommunity(long communityId) {
        boolean found = false;
        Iterator<Community> it = applicationCommunities.iterator();
        // removing from list by using iterator
        while (it.hasNext()) {
            Community c = it.next();
            if (c.getId() == communityId) {
                found = true;
                it.remove();
                break;
            }
        }

        if (!found) {
            throw new IllegalArgumentException("Community with id " + communityId + " not found");
        }
    }

    public void listCommunities() {
        if (applicationCommunities.isEmpty()) {
            System.out.println("No communities to list!");
            return;
        }
        for (Community community : applicationCommunities) {
            System.out.println(community);
        }
    }

    public void joinCommunity(Community community, User user) {
        // right now, join means immediate approval into the community since we don't have admins or moderators yet
        if (community.findUserById(user.getUserId()) != null) {
            throw new IllegalArgumentException("User is already part of the community");
        }

        community.addUser(user);
    }

    public void exitCommunity(Community community, User user) {
        // exiting doesn't need approval
        // if the community has only one user then delete the community

        // check that the person is part of the community
        if (community.findUserById(user.getUserId()) == null) {
            throw new IllegalArgumentException("User is not part of the community");
        }

        if (community.getCommunityUsers().size() == 1) {
            throw new IllegalStateException("You are the last member. You cannot exit the community.");
        } else {
            // exit means removing person from community s communityUsers list
            community.removeUser(user.getUserId());
        }
    }

    public void removePostFromCommunity(Community community, Post post) {
        community.removePost(post.getPostId());
    }

    public Community addCommunity(Community community) {

        if (h2CommunityRepository.existsByCommunityName(community.getCommunityName())) {
            throw new IllegalArgumentException("Community name is already taken");
        }

        community.setId(null);
        return h2CommunityRepository.save(community);
    }
}
