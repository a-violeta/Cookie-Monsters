package com.app.service;

import com.app.model.Comment;

import java.util.List;
import java.util.UUID;

public interface CommentUseCases {
    void validateComment(String text);
    Comment addComment(String text, long userId, UUID postId);
    Comment findCommentById(long commentId);
    void editComment(long commentId, String newText);
    void removeComment(long commentId);
    List<Comment> listComments();
    List<Comment> listCommentByPostId(UUID postId);
}