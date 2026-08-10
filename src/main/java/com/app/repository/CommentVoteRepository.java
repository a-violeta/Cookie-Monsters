package com.app.repository;

import com.app.model.Comment;
import com.app.model.CommentVote;
import com.app.model.User;

import java.util.Optional;

public interface CommentVoteRepository {
    CommentVote save(CommentVote vote);
    Optional<CommentVote> findByCommentAndAuthor(Comment comment, User currentUser);
}
