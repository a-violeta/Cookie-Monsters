package com.app.repository;

import com.app.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaCommentRepository extends JpaRepository<Comment, Long>, CommentRepository {
}
