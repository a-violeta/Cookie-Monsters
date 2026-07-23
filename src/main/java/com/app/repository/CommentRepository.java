package com.app.repository;

import org.springframework.stereotype.Repository;
import com.app.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    // JpaRepository already give us these method > save(), findById(), deleteById()
}
