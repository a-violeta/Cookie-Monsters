package com.app.service;

import com.app.model.Comment;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Getter
@Service
public class CommentService implements CommentUseCases{

    private List<Comment> applicationComments = new ArrayList<>();

    public void validateComment(String text) {
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("Comment text is required");
        }
    }

    public Comment addComment(String text, long userId, long postId) {
        validateComment(text);
        Comment newComment = new Comment(text, userId, postId);
        applicationComments.add(newComment);
        return newComment;
    }

    public Comment findCommentById(long commentId) {
        for (Comment comment : applicationComments) {
            if (comment.getCommentId() == commentId) {
                return comment;
            }
        }

        return null;
    }

    public void editComment(long commentId, String newText) {
        Comment comment = findCommentById(commentId);
        if (comment != null) {
            validateComment(newText);
            comment.setText(newText);
        } else {
            throw new IllegalArgumentException("Comment with id " + commentId + " not found");
        }
    }

    public void removeComment(long commentId) {
        Comment comment = findCommentById(commentId);
        if (comment != null) {
            applicationComments.remove(comment);
        } else {
            throw new IllegalArgumentException("Comment with id " + commentId + " not found");
        }
    }
}

