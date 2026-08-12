package com.app.service;

import com.app.model.Comment;

import java.util.List;
import java.util.UUID;

public interface CommentAbstract {
    void validateComment(String text);
    Comment addComment(String text, UUID postId, UUID parentId, String requesterUsername);
    Comment findCommentById(UUID commentId, String requesterUsername);
    Comment editComment(UUID commentId, String newText, String requesterUsername);
    void removeComment(UUID commentId, String requesterUsername);
    List<Comment> listCommentByPostId(UUID postId, String requesterUsername);
    Comment voteComment(UUID commentId,String voteType, String requesterUsername);
}
