package com.app.service;

import com.app.model.Comment;
import com.app.model.Post;
import com.app.model.User;
import com.app.repository.CommentRepository;
import com.app.repository.PostRepository;
import com.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.http.client.enabled", havingValue = "false", matchIfMissing = true)
public class CommentService implements CommentUseCases {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final UserUseCases userUseCases;

    public void validateComment(String text) {
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("Comment text is required");
        }
    }

    @Transactional
    public Comment addComment(String text, UUID postId, UUID parentId, String creatorUsername) {

        validateComment(text);

        User author = userRepository.findByUsername(creatorUsername)
                .orElseThrow(() -> new IllegalStateException("Authenticated user not found"));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post with id " + postId + " not found"));

        Comment newComment = new Comment();
        newComment.setContent(text);
        newComment.setAuthor(author);
        newComment.setPost(post);
        newComment.setCreatedAt(LocalDateTime.now());

        // Check for not null parentId
        if (parentId != null) {
            Comment parent = findCommentById(parentId);

            // Check that the comment with the parentId as the same postId as the reply
            if (!parent.getPost().getId().equals(postId)) {
                throw new IllegalArgumentException("Parent comment belongs to a different post");
            }
            newComment.setParent(parent);
        }

        post.setCommentCount(post.getCommentCount() + 1);
        postRepository.save(post);

        newComment.setUpvotes(1);
        newComment.setScore(1);

        return commentRepository.save(newComment);
    }

    @Transactional(readOnly = true)
    public Comment findCommentById(UUID commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment with id " + commentId + " not found"));
    }

    @Transactional(readOnly = true)
    public List<Comment> listComments() {
        return commentRepository.findAll();
    }

    @Transactional
    public List<Comment> listCommentByPostId(UUID postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post with id " + postId + " not found"));

        List<Comment> comments = new ArrayList<>();
        for (Comment comment : commentRepository.findAll()) {
            if (Objects.equals(comment.getPost(), post)) {
                comments.add(comment);
            }
        }
        return comments;
    }

    @Transactional
    public void editComment(UUID commentId, String newText) {
        Comment comment = findCommentById(commentId);

        if (!Objects.equals(comment.getAuthor().getId(), userUseCases.getLoggedInUser().getId())) {
            throw new IllegalStateException("This comment was not created by You ");
        }
        validateComment(newText);
        comment.setContent(newText);
        commentRepository.save(comment);
    }

    @Transactional
    public void removeComment(UUID commentId) {
        // try to find this comment
        Comment comment = findCommentById(commentId);

        if (!Objects.equals(comment.getAuthor().getId(), userUseCases.getLoggedInUser().getId())) {
            throw new IllegalStateException("This comment was not created by You ");
        }

        Post post = comment.getPost();
        post.setCommentCount(post.getCommentCount() - 1);
        postRepository.save(post);

        commentRepository.deleteById(commentId);
    }
}

