package com.app.repository;

import org.springframework.stereotype.Repository;
import com.app.model.Comment;
import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository {
    Optional<Comment> findById(Long id);
    Comment save(Comment comment);
    void delete(Comment comment);
    List<Comment> findAll();
    boolean existsById(Long id);
    void deleteById(Long id);
}
