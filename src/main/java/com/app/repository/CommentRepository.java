package com.app.repository;

import com.app.model.Post;
import org.springframework.stereotype.Repository;
import com.app.model.Comment;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CommentRepository {
    Optional<Comment> findById(UUID id);
    Comment save(Comment comment);
    void delete(Comment comment);
    List<Comment> findAll();
    List<Comment> findAllByPost(Post post);
    boolean existsById(UUID id);
    void deleteById(UUID id);
}
