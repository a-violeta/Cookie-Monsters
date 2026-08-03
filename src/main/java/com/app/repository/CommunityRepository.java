package com.app.repository;

import com.app.model.Community;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CommunityRepository {
    Optional<Community> findById(UUID id);
    Community save(Community community);
    void delete(Community community);
    List<Community> findAll();
    boolean existsByName(String name);
}