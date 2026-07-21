package com.app.service;

import com.app.model.Community;
import com.app.model.Post;
import com.app.model.User;
import com.app.repository.CommunityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CommunityService {

    private final CommunityRepository communityRepository;

    public Community findCommunityById(long communityId) {
        return communityRepository.findById(communityId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Community with id " + communityId + " not found"
                        ));
    }

    public void editCommunity(long communityId, String description) {
        Community community = findCommunityById(communityId);
        if (community != null) {
            community.setDescription(description);
            communityRepository.save(community);
        } else {
            throw new IllegalArgumentException("Community with id " + communityId + " not found");
        }
    }

    public void deleteCommunity(long communityId) {

        Community community = findCommunityById(communityId);

        if(community!=null){
            communityRepository.delete(community);
        }
        else{
            throw new IllegalArgumentException("Community with id " + communityId + " not found");
        }
    }

    public void listCommunities() {
        if (communityRepository.findAll().isEmpty()) {
            System.out.println("No communities to list!");
            return;
        }
        for (Community community : communityRepository.findAll()) {
            System.out.println(community);
        }
    }

    public void joinCommunity(Community community, User user) {
        // right now, join means immediate approval into the community since we don't have admins or moderators yet
        if (community.findUserById(user.getUserId()) != null) {
            throw new IllegalArgumentException("User is already part of the community");
        }

        community.addUser(user);
        communityRepository.save(community);
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
            communityRepository.save(community);
        }
    }

    public void removePostFromCommunity(Community community, Post post) {
        community.removePost(post.getPostId());
        communityRepository.save(community);
    }

    public Community addCommunity(Community community) {

        if (communityRepository.existsByCommunityName(community.getCommunityName())) {
            throw new IllegalArgumentException("Community name is already taken");
        }

        community.setId(null);
        return communityRepository.save(community);
    }
}
