package com.app.repository;

import com.app.model.Community;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CommunityRepository {
    // Fixed type to UUID to match the entity
    Optional<Community> findById(UUID id);
    Community save(Community community);
    void delete(Community community);
    List<Community> findAll();

    // Fixed method name to match the field 'name' in Community entity
    boolean existsByName(String name);

    List<Community> findAllByCommunityUsers_Id(Long userId);
}