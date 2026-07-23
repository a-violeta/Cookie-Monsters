package com.app.service;

import com.app.model.Comment;
import com.app.model.Post;
import com.app.model.User;
import com.app.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService implements CommentUseCases{

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public void validateComment(String text) {
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("Comment text is required");
        }
    }

    @Transactional
    public Comment addComment(String text, long userId, long postId) {
        validateComment(text);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with id " + userId + " not found"));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post with id " + postId + " not found"));

        if (post.getCommunity().findUserById(userId) == null) {
            throw new IllegalArgumentException("User is not a member of the community this post belongs to");
        }

        Comment newComment = new Comment();
        newComment.setText(text);
        newComment.setUser(user);
        newComment.setPost(post);
        newComment.setCreatedAt(LocalDateTime.now());

        return commentRepository.save(newComment);
    }

    @Transactional(readOnly = true)
    public Comment findCommentById(long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment with id " + commentId + " not found"));
    }

    @Transactional(readOnly = true)
    public List<Comment> listComments() {
        return commentRepository.findAll();
    }

    @Transactional
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

    @Transactional
    public void removeComment(long commentId) {
        // try to find this comment
        Comment comment = findCommentById(commentId);

        // if not found, method throws exception
        // if found, we delete
        commentRepository.deleteById(commentId);
    }
}

