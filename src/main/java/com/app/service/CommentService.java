package com.app.service;

import com.app.model.*;
import com.app.repository.CommentRepository;
import com.app.repository.PostRepository;
import com.app.repository.UserRepository;
import com.app.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
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
    private final VoteRepository voteRepository;

    public void validateComment(String text) {
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("Comment text is required");
        }
    }

    private void populateUserVoteStatus(Comment comment, User currentUser) {
        if (currentUser == null) {
            return;
        }

        voteRepository.findByCommentAndAuthor(comment, currentUser).ifPresent(vote -> {
            if (vote.getUserVote() != null) {
                comment.setUserVote(vote.getUserVote().toString().toLowerCase());
            }
        });
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
        newComment.setUpdatedAt(Instant.now());
        newComment.setCreatedAt(Instant.now());

        // Check for not null parentId
        if (parentId != null) {
            Comment parent = findCommentById(parentId, creatorUsername);

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
    public Comment findCommentById(UUID commentId, String requesterUsername) {

        User requester = userRepository.findByUsername(requesterUsername)
                .orElseThrow(() -> new IllegalStateException("Authenticated user not found"));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment with id " + commentId + " not found"));
        populateUserVoteStatus(comment, requester);
        return comment;
    }

    @Transactional(readOnly = true)
    public List<Comment> listComments() {
        return commentRepository.findAll();
    }

    @Transactional
    public List<Comment> listCommentByPostId(UUID postId, String requesterUsername) {

        User requester = userRepository.findByUsername(requesterUsername)
                .orElseThrow(() -> new IllegalStateException("Authenticated user not found"));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post with id " + postId + " not found"));

        List<Comment> comments = commentRepository.findAllByPost(post);

        comments.forEach(comment -> populateUserVoteStatus(comment, requester));

        return comments;
    }

    @Transactional
    public Comment editComment(UUID commentId, String newText, String requesterUsername) {

        Comment comment = findCommentById(commentId, requesterUsername );

        User author = userRepository.findByUsername(requesterUsername)
                .orElseThrow(() -> new IllegalStateException("Authenticated user not found"));

        if (!Objects.equals(comment.getAuthor().getId(), author.getId())) {
            throw new IllegalStateException("This comment was not created by you");
        }

        validateComment(newText);
        comment.setContent(newText);
        comment.setUpdatedAt(Instant.now());
        commentRepository.save(comment);
        return comment;
    }

    @Transactional
    public void removeComment(UUID commentId, String requesterUsername) {
        // try to find this comment
        Comment comment = findCommentById(commentId, requesterUsername );

        User author = userRepository.findByUsername(requesterUsername)
                .orElseThrow(() -> new IllegalStateException("Authenticated user not found"));

        if (!Objects.equals(comment.getAuthor(), author)) {
            throw new IllegalStateException("This comment was not created by you");
        }

        Post post = comment.getPost();
        post.setCommentCount(post.getCommentCount() - 1);
        postRepository.save(post);

        commentRepository.deleteById(commentId);
    }

    @Transactional
    public Comment votePost(UUID id, String voteType, String requesterUsername) {

        Comment comment = findCommentById(id, requesterUsername);

        User requester = userRepository.findByUsername(requesterUsername)
                .orElseThrow(() -> new IllegalArgumentException("User " + requesterUsername + " not found"));

        Vote vote = voteRepository.findByCommentAndAuthor(comment, requester).orElse(null);

        if (vote == null) {
            vote = new Vote();
            vote.setComment(comment);
            vote.setAuthor(requester);
        }

        VoteType currentVote = vote.getUserVote();

        // toggle logic: if voteType in request is the same as current vote, user intention is to cancel the vote
        if (("up".equals(voteType) && currentVote == VoteType.UP) ||
                ("down".equals(voteType) && currentVote == VoteType.DOWN)) {
            voteType = "none";
        }

        if (currentVote == VoteType.UP) {
            comment.setUpvotes(comment.getUpvotes() - 1);
        } else if (currentVote == VoteType.DOWN) {
            comment.setDownvotes(comment.getDownvotes() - 1);
        }

        switch (voteType) {
            case "up" -> {
                comment.setUpvotes(comment.getUpvotes() + 1);
                vote.setUserVote(VoteType.UP);
            }
            case "down" -> {
                comment.setDownvotes(comment.getDownvotes() + 1);
                vote.setUserVote(VoteType.DOWN);
            }
            case "none" -> vote.setUserVote(null);
            case null, default -> throw new IllegalArgumentException("Invalid vote.");
        }

        comment.setScore(comment.getUpvotes() - comment.getDownvotes());

        if (vote.getUserVote() != null) {
            comment.setUserVote(vote.getUserVote().toString().toLowerCase());
        } else {
            comment.setUserVote(null);
        }

        comment.setUpdatedAt(Instant.now());

        voteRepository.save(vote);
        commentRepository.save(comment);
        return comment;
    }
}

