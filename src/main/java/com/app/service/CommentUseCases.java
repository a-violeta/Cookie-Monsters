package com.app.service;

import com.app.model.Comment;

import java.util.List;
import java.util.UUID;

public interface CommentUseCases {
    void validateComment(String text);
    Comment addComment(String text, UUID postId, UUID parentId, String creatorUsername);
    Comment findCommentById(UUID commentId, String requesterUsername);
    Comment editComment(UUID commentId, String newText, String requesterUsername);
    void removeComment(UUID commentId, String requesterUsername);
    List<Comment> listComments();
    List<Comment> listCommentByPostId(UUID postId, String requesterUsername);
}
