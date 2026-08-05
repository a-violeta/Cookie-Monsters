package com.app.service;

import com.app.model.Community;
import com.app.model.Post;

import java.util.List;
import java.util.UUID;

public interface CommunityUseCases {
    // Fixed method to match the exact arguments requested across controllers, commands, and tests
    Community createCommunity(String name, String displayName, String description, String iconUrl);
    void validateCommunity(String name, String description);
    void deleteCommunity(String name);
    List<Community> listCommunities();
    Community findCommunityById(UUID communityId);
    Community findCommunityByName(String name);
    void editCommunity(String name, String displayName, String description);
    void joinCommunity(UUID communityId, Long userId);
    void exitCommunity(UUID communityId, Long userId);
    List<Community> listCommunitiesByUserId(Long userId);

    // Added the missing method used by CommunityController
    List<Post> listCommunityPosts(String name);
}