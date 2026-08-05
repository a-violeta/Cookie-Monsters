package com.app.service;

import com.app.model.Community;
import com.app.model.Post;

import java.util.List;
import java.util.UUID;

public interface CommunityUseCases {
    void validateCommunity(String communityName, String displayName, String description);

    Community createCommunity(String name, String displayName, String description, String iconUrl);
    void deleteCommunity(String name);
    List<Community> listCommunities();
    Community findCommunityById(UUID communityId);
    Community findCommunityByName(String name);
    void editCommunity(String name, String displayName, String iconUrl, String description);
    void joinCommunity(UUID communityId, Long userId);
    void exitCommunity(UUID communityId, Long userId);
    List<Community> listCommunitiesByUserId(Long userId);
    List<Post> listCommunityPosts(String name);
}