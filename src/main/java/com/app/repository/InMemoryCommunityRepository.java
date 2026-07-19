package com.app.repository;

import com.app.model.Community;
import org.springframework.context.annotation.Primary;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Primary
// @Primary tells Spring which repo to choose for CommunityService
// because in CommunityService we have an instance of CommunityRepository which can be JpaCommunityRepository or InMemoryCommunityRepository
// without @Primary Spring would not know what bean to choose
public class InMemoryCommunityRepository implements CommunityRepository {

    // this is our storage
    private final Map<Long, Community> storage = new ConcurrentHashMap<>();

    // simulates GenerationType.IDENTITY auto-increment
    private final AtomicLong idSequence = new AtomicLong(1);

    @Override
    public Optional<Community> findById(Long id) {
        // can be null if not found
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public Community save(Community community) {
        if (community.getId() == null) { // this is an insert, otherwise it s an update
            community.setId(idSequence.getAndIncrement());
        }
        storage.put(community.getId(), community);
        return community;
    }

    @Override
    public void delete(Community community) {
        storage.remove(community.getId());
    }

    @Override
    public List<Community> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public boolean existsByCommunityName(String communityName) {
        return storage.values().stream()
                .anyMatch(c -> c.getCommunityName().equals(communityName));
    }
}