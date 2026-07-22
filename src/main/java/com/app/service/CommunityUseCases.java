package com.app.service;

import com.app.model.Community;

public interface CommunityUseCases {
    // method useful for the CLI
    Community createCommunity(String communityName, String description);
    Community addCommunity(Community community);
    void deleteCommunity(long communityId);
    void listCommunities();
    Community findCommunityById(long communityId);
    void editCommunity(long communityId, String description);
    void joinCommunity(Long communityId, Long userId);
    void exitCommunity(Long communityId, Long userId);
    void removePostFromCommunity(Long communityId, Long postId);
}