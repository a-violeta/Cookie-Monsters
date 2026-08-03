package com.app.repository;

import com.app.model.Post;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PostRepository {
    Optional<Post> findById(UUID id);
    Post save(Post post);
    void delete(Post post);
    List<Post> findAll();
}
