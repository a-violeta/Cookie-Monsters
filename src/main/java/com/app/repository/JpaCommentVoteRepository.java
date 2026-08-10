package com.app.repository;

import com.app.model.CommentVote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaCommentVoteRepository extends JpaRepository<CommentVote, UUID>, CommentVoteRepository {
}
