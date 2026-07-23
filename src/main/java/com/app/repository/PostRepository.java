package com.app.repository;

import com.app.model.Community;
import com.app.model.Post;

import java.util.List;
import java.util.Optional;

public interface PostRepository {
    Optional<Post> findById(Long id);
    Post save(Post post);
    void delete(Post post);
    List<Post> findAll();
}
