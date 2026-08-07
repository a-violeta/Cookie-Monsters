package com.app.service;

import com.app.model.Comment;

import java.util.List;
import java.util.UUID;

public interface CommentUseCases {
    void validateComment(String text);
    Comment addComment(String text, UUID postId, UUID parentId, String creatorUsername);
    Comment findCommentById(UUID commentId);
    void editComment(UUID commentId, String newText);
    void removeComment(UUID commentId);
    List<Comment> listComments();
    List<Comment> listCommentByPostId(UUID postId);
}
