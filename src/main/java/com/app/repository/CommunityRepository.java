package com.app.repository;

import com.app.model.Community;

import java.util.List;
import java.util.Optional;

public interface CommunityRepository {
    Optional<Community> findById(Long id);
    Community save(Community community);
    void delete(Community community);
    List<Community> findAll();
    boolean existsByCommunityName(String communityName);
}