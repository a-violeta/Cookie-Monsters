package com.app.service;

import com.app.model.Community;
import com.app.model.Post;
import com.app.model.User;
import com.app.repository.CommunityRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.handler.UserRoleAuthorizationInterceptor;

import java.util.ArrayList;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Getter
public class CommunityService implements CommunityUseCases {

    private final CommunityRepository communityRepository;
    //private final UserRepository userRepository;
    //private final PostRepository postRepository;

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

    public void joinCommunity(Long communityId, Long userId) {
        // right now, join means immediate approval into the community since we don't have admins or moderators yet
        Community community = findCommunityById(communityId);
        //User user = userRepository.findById(userId)
                //.orElseThrow(() -> new IllegalArgumentException("User with id " + userId + " not found"));

        if (community.findUserById(userId) != null) {
            throw new IllegalArgumentException("User is already part of the community");
        }

        //community.addUser(user);
        communityRepository.save(community);
    }

    public void exitCommunity(Long communityId, Long userId) {
        // exiting doesn't need approval
        // if the community has only one user then delete the community
        Community community = findCommunityById(communityId);

        // check that the person is part of the community
        if (community.findUserById(userId) == null) {
            throw new IllegalArgumentException("User is not part of the community");
        }

        if (community.getCommunityUsers().size() == 1) {
            throw new IllegalStateException("You are the last member. You cannot exit the community.");
        } else {
            // exit means removing person from community s communityUsers list
            community.removeUser(userId);
            communityRepository.save(community);
        }
    }

    public void removePostFromCommunity(Long communityId, Long postId) {
        Community community = findCommunityById(communityId);
        community.removePost(postId);
        communityRepository.save(community);
    }

    public Community addCommunity(Community community) {

        if (communityRepository.existsByCommunityName(community.getCommunityName())) {
            throw new IllegalArgumentException("Community name is already taken");
        }

        community.setId(null);
        return communityRepository.save(community);
    }

    // method necessary because in console you can't pass a Community as an argument
    public Community createCommunity(String communityName, String description){

        if (communityRepository.existsByCommunityName(communityName)) {
            throw new IllegalArgumentException("Community name is already taken");
        }
        Community community = new Community(communityName, description, new ArrayList<>(), new ArrayList<>());
        return communityRepository.save(community);
    }
}
