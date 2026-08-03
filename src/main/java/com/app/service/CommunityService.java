package com.app.service;

import com.app.model.Community;
import com.app.model.Post;
import com.app.model.User;
import com.app.repository.CommunityRepository;
import com.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.http.client.enabled", havingValue = "false", matchIfMissing = true)
public class CommunityService implements CommunityUseCases {

    private final CommunityRepository communityRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public void validateCommunity(String name, String displayName, String description) {
        if (name == null || name.isBlank() || displayName == null || displayName.isBlank()) {
            throw new IllegalArgumentException("Community name is required");
        }

        if (!name.matches("^[a-zA-Z0-9_]+$")) {
            throw new IllegalArgumentException("Community name must contain only letters, numbers, and '_'");
        }

        if (name.length() < 3 || displayName.length() < 3) {
            throw new IllegalArgumentException("Community name must have at least 3 characters");
        }

        if (name.length() > 50 || displayName.length() > 50) {
            throw new IllegalArgumentException("Community name is too long");
        }

        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Community description is required");
        }

        if (description.length() > 500) {
            throw new IllegalArgumentException("Description is too long");
        }
    }

    @Transactional(readOnly = true)
    public Community findCommunityById(UUID communityId) {
        return communityRepository.findById(communityId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Community with id " + communityId + " not found"
                        ));
    }

    public Community findCommunityByName(String name) {
        for (Community c : communityRepository.findAll()) {
            if (Objects.equals(c.getName().toLowerCase(), name.toLowerCase())) {
                return c;
            }
        }
        throw new IllegalArgumentException("Community with name " + name + " not found");
    }

    @Transactional
    public void editCommunity(String name, String displayName, String description) {
        Community community = findCommunityByName(name);

        if (!community.getCommunityUsers().contains(userService.getLoggedInUser())) {
            throw new IllegalArgumentException("You are not a member of this community");
        }

        validateCommunity(community.getName(), displayName, description);

        // just a user from that community should be able to edit
        if (!community.getCommunityUsers().contains(userService.getLoggedInUser())) {
            throw new IllegalStateException("User is not in community!");
        }

        community.setDescription(description);
        community.setDisplayName(displayName);
        communityRepository.save(community);
    }

    @Transactional
    public void deleteCommunity(String name) {
        Community community = findCommunityByName(name);

        if (!community.getCommunityUsers().contains(userService.getLoggedInUser())) {
            throw new IllegalArgumentException("You are not a member of this community");
        }

        communityRepository.delete(community);

    }

    @Transactional(readOnly = true)
    public List<Community> listCommunities() {

        return communityRepository.findAll();
    }

    @Transactional
    public void joinCommunity(UUID communityId, Long userId) {
        // right now, join means immediate approval into the community
        Community community = findCommunityById(communityId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with id " + userId + " not found"));

        if (community.findUserById(userId) != null) {
            throw new IllegalArgumentException("User is already part of the community");
        }

        community.getCommunityUsers().add(user);
        communityRepository.save(community);
    }

    @Transactional
    public void exitCommunity(UUID communityId, Long userId) {
        // exiting doesn't need approval either
        // if the community has only one user then tell user to delete the community instead
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
        while (it.hasNext()) {
            User u = it.next();
            if (Objects.equals(u.getId(), userId)) {
                it.remove();
                break;
            }
        }

        communityRepository.save(community);
    }

    @Transactional
    public Community addCommunity(Community community) {

        if (communityRepository.existsByName(community.getName())) {
            throw new IllegalArgumentException("Community name is already taken");
        }

        community.setId(null);
        return communityRepository.save(community);
    }

    @Transactional
    public Community createCommunity(String communityName, String displayName, String description, String iconUrl) {
        validateCommunity(communityName, displayName, description);

        if (communityRepository.existsByName(communityName)) {
            throw new IllegalArgumentException("Community name is already taken");
        }
        Community community = new Community();
        community.setName(communityName);
        community.setDisplayName(displayName);
        community.setDescription(description);
        community.setIconUrl(iconUrl);

        // also, take the active user and add him to the community members
        User currentUser = userService.getLoggedInUser();

        if (currentUser == null) {
            throw new IllegalStateException("You must be logged in to create a community");
        }

        List<User> communityMembers = new ArrayList<>();
        communityMembers.add(currentUser);
        community.setCommunityUsers(communityMembers);

        return communityRepository.save(community);
    }

    @Transactional(readOnly = true)
    public List<Post> listCommunityPosts(String name){
        Community community = findCommunityByName(name);

        return community.getCommunityPosts();
    }
}
