package com.app.service;

import com.app.model.Comment;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.UUID;

public interface CommentUseCases {
    void validateComment(String text);
    Comment addComment(String text, UUID postId, UUID parentId, Authentication authentication);
    Comment findCommentById(UUID commentId, Authentication authentication);
    Comment editComment(UUID commentId, String newText, Authentication authentication);
    void removeComment(UUID commentId, Authentication authentication);
    List<Comment> listCommentByPostId(UUID postId, Authentication authentication);
    Comment voteComment(UUID commentId,String voteType, Authentication authentication);
}
