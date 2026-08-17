package com.app.repository;

import com.app.model.Comment;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

@Primary
// fixed the ID type from Long to UUID to match the exact Comment entity primary key
// (every other Jpa*Repository was already fixed this way - this one was missed)
public interface JpaCommentRepository extends JpaRepository<Comment, UUID>, CommentRepository {
}