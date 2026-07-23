package com.app.service;

import com.app.model.Community;
import com.app.model.User;
import com.app.repository.CommunityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CommunityService implements CommunityUseCases {

    private final CommunityRepository communityRepository;
    //private final UserRepository userRepository;
    //private final PostRepository postRepository;

    @Transactional(readOnly = true)
    public Community findCommunityById(long communityId) {
        return communityRepository.findById(communityId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Community with id " + communityId + " not found"
                        ));
    }

    @Transactional
    public void editCommunity(long communityId, String description) {
        Community community = findCommunityById(communityId);
        if (community != null) {
            community.setDescription(description);
            communityRepository.save(community);
        } else {
            throw new IllegalArgumentException("Community with id " + communityId + " not found");
        }
    }

    @Transactional
    public void deleteCommunity(long communityId) {

        Community community = findCommunityById(communityId);

        if(community!=null){
            communityRepository.delete(community);
        }
        else{
            throw new IllegalArgumentException("Community with id " + communityId + " not found");
        }
    }

    @Transactional(readOnly = true)
    public List<Community> listCommunities() {

        return communityRepository.findAll();
    }

    @Transactional
    public void joinCommunity(Long communityId, Long userId) {
        // right now, join means immediate approval into the community since we don't have admins or moderators yet
        Community community = findCommunityById(communityId);
        //User user = userRepository.findById(userId)
                //.orElseThrow(() -> new IllegalArgumentException("User with id " + userId + " not found"));

        if (community.findUserById(userId) != null) {
            throw new IllegalArgumentException("User is already part of the community");
        }

        //community.getCommunityUsers().add(user);
        communityRepository.save(community);
    }

    @Transactional
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
        }

        // exit means removing person from community s communityUsers list

        Iterator<User> it = community.getCommunityUsers().iterator();
        // removing from list by using iterator
        while(it.hasNext()){
            User u = it.next();
            if(Objects.equals(u.getUserId(), userId)){
                it.remove();
                break;
            }
        }

        communityRepository.save(community);
    }

    @Transactional
    public Community addCommunity(Community community) {

        if (communityRepository.existsByCommunityName(community.getCommunityName())) {
            throw new IllegalArgumentException("Community name is already taken");
        }

        community.setCommunityId(null);
        return communityRepository.save(community);
    }

    // method necessary because in console you can't pass a Community as an argument
    @Transactional
    public Community createCommunity(String communityName, String description){

        if (communityRepository.existsByCommunityName(communityName)) {
            throw new IllegalArgumentException("Community name is already taken");
        }
        Community community = new Community();
        community.setCommunityName(communityName);
        community.setDescription(description);

        // also, take the active user and add him to the community members

        return communityRepository.save(community);
    }
}
