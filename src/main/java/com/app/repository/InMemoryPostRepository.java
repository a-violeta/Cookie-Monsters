package com.app.repository;

import com.app.model.Post;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

// not really using this anymore, we have a DB

@Repository
public class InMemoryPostRepository implements PostRepository {
    // this is our storage
    private final Map<UUID, Post> storage = new ConcurrentHashMap<>();

    @Override
    public Optional<Post> findById(UUID id) {
        // can be null if not found
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public Post save(Post post) {
        if (post.getId() == null) { // this is an insert, otherwise it's an update
            post.setId(UUID.randomUUID());
        }
        storage.put(post.getId(), post);
        return post;
    }

    @Override
    public void delete(Post post) {
        storage.remove(post.getId());
    }

    @Override
    public List<Post> findAll() {
        return new ArrayList<>(storage.values());
    }
}
