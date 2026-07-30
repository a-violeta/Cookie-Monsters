package com.app.service;

import com.app.model.Community;

import java.util.List;

public interface CommunityUseCases {
    Community createCommunity(String communityName, String description);
    void validateCommunity(String communityName, String description);
    void deleteCommunity(long communityId);
    List<Community> listCommunities();
    Community findCommunityById(long communityId);
    Community findCommunityByName(String name);
    void editCommunity(long communityId, String description);
    void joinCommunity(Long communityId, Long userId);
    void exitCommunity(Long communityId, Long userId);
}