package com.app.service;

import com.app.model.Community;
import com.app.model.Post;

import java.util.List;
import java.util.UUID;

public interface CommunityUseCases {
    // validateCommunity is copied here because it s just logic, no DB access
    // round-tripping the network just to do a check would be wasteful
    // and the two copies are kept in sync since neither needs the DB
    // UserService.createUser's validation DOES need the DB to check username uniqueness
    // and so createUser is never duplicated
    void validateCommunity(String communityName, String displayName, String description);

    // Fixed method to match the exact arguments requested across controllers, commands, and tests
    Community createCommunity(String name, String displayName, String description, String iconUrl);
    void validateCommunity(String name, String description);
    void deleteCommunity(String name);
    List<Community> listCommunities();
    Community findCommunityById(UUID communityId);
    Community findCommunityByName(String name);
    void editCommunity(String name, String displayName, String iconUrl, String description);
    void joinCommunity(UUID communityId, Long userId);
    void exitCommunity(UUID communityId, Long userId);
    List<Community> listCommunitiesByUserId(Long userId);

    // Added the missing method used by CommunityController
    List<Post> listCommunityPosts(String name);
}