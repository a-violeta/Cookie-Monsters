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
@Getter
public class CommunityService {

    private final H2CommunityRepository h2CommunityRepository;

    public Community findCommunityById(long communityId) {
        return h2CommunityRepository.findById(communityId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Community with id " + communityId + " not found"
                        ));
    }

    public void editCommunity(long communityId, String description) {
        Community community = findCommunityById(communityId);
        if (community != null) {
            community.setDescription(description);
            h2CommunityRepository.save(community);
        } else {
            throw new IllegalArgumentException("Community with id " + communityId + " not found");
        }
    }

    public void deleteCommunity(long communityId) {

        Community community = findCommunityById(communityId);

        if(community!=null){
            h2CommunityRepository.delete(community);
        }
        else{
            throw new IllegalArgumentException("Community with id " + communityId + " not found");
        }
    }

    public void listCommunities() {
        if (h2CommunityRepository.findAll().isEmpty()) {
            System.out.println("No communities to list!");
            return;
        }
        for (Community community : h2CommunityRepository.findAll()) {
            System.out.println(community);
        }
    }

    public void joinCommunity(Community community, User user) {
        // right now, join means immediate approval into the community since we don't have admins or moderators yet
        if (community.findUserById(user.getUserId()) != null) {
            throw new IllegalArgumentException("User is already part of the community");
        }

        community.addUser(user);
        h2CommunityRepository.save(community);
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
            h2CommunityRepository.save(community);
        }
    }

    public void removePostFromCommunity(Community community, Post post) {
        community.removePost(post.getPostId());
        h2CommunityRepository.save(community);
    }

    public Community addCommunity(Community community) {

        if (h2CommunityRepository.existsByCommunityName(community.getCommunityName())) {
            throw new IllegalArgumentException("Community name is already taken");
        }

        community.setId(null);
        return h2CommunityRepository.save(community);
    }
}
