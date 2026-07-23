package com.app.service;

import com.app.model.Comment;
import com.app.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CommentService implements CommentUseCases{

    private final CommentRepository commentRepository;

    public void validateComment(String text) {
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("Comment text is required");
        }
    }

    public Comment addComment(String text, long userId, long postId) {
        validateComment(text);
        Comment newComment = new Comment(text, userId, postId);
        return commentRepository.save(newComment);
    }

    public Comment findCommentById(long commentId) {
        return commentRepository.findById(commentId).orElse(null);
    }

    public void editComment(long commentId, String newText) {
        Comment comment = findCommentById(commentId);
        if (comment != null) {
            validateComment(newText);
            comment.setText(newText);
            commentRepository.save(comment);
        } else {
            throw new IllegalArgumentException("Comment with id " + commentId + " not found");
        }
    }

    public void removeComment(long commentId) {
        if (commentRepository.existsById(commentId)) {
            commentRepository.deleteById(commentId);
        } else {
            throw new IllegalArgumentException("Comment with id " + commentId + " not found");
        }
    }
}

