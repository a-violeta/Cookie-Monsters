package com.app.repository;

import com.app.model.Community;
import com.app.model.Post;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
@Primary
public class InMemoryPostRepository implements PostRepository {
    // this is our storage
    private final Map<Long, Post> storage = new ConcurrentHashMap<>();

    // simulates GenerationType.IDENTITY auto-increment
    private final AtomicLong idSequence = new AtomicLong(1);

    @Override
    public Optional<Post> findById(Long id) {
        // can be null if not found
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public Post save(Post post) {
        if (post.getId() == null) { // this is an insert, otherwise it's an update
            post.setId(idSequence.getAndIncrement());
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
