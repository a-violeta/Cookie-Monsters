package com.app.service;

import com.app.model.Comment;

public interface CommentUseCases {
    void validateComment(String text);
    Comment addComment(String text, long userId, long postId);
    Comment findCommentById(long commentId);
    void editComment(long commentId, String newText);
    void removeComment(long commentId);
}
