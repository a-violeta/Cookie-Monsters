package com.app.service;

import ch.qos.logback.classic.spi.IThrowableProxy;
import com.app.model.*;
import com.app.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.http.client.enabled", havingValue = "false", matchIfMissing = true)
public class CommentService implements CommentAbstract {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentVoteRepository commentVoteRepository;

    public void validateComment(String text) {
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("Comment text is required");
        }
    }

    private void populateUserVoteStatus(Comment comment, User currentUser) {
        if (currentUser == null) {
            return;
        }

        commentVoteRepository.findByCommentAndAuthor(comment, currentUser).ifPresent(vote -> {
            if (vote.getVoteType() != null) {
                comment.setUserVote(vote.getVoteType().toString().toLowerCase());
            }
        });
    }

    @Transactional
    public Comment addComment(String text, UUID postId, UUID parentId, String requesterUsername) {

        validateComment(text);

        User author = userRepository.findByUsername(requesterUsername)
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
            Comment parent = findCommentById(parentId, requesterUsername);

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
        newComment.setUserVote("up");

        CommentVote commentVote = new CommentVote();
        commentVote.setAuthor(author);
        commentVote.setComment(newComment);
        commentVote.setVoteType(VoteType.UP);
        commentVoteRepository.save(commentVote);

        return commentRepository.save(newComment);
    }

    @Transactional(readOnly = true)
    public Comment findCommentById(UUID commentId, String requesterUsername) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment with id " + commentId + " not found"));

        User requester = null;

        if  (requesterUsername != null) {
            requester = userRepository.findByUsername(requesterUsername).orElse(null);
        }

        final User finalRequester = requester;

        populateUserVoteStatus(comment, finalRequester);

        comment.getReplies().size();

        return comment;
    }

    @Transactional
    public List<Comment> listCommentByPostId(UUID postId, String requesterUsername) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post with id " + postId + " not found"));

        // Create a non-logged-in user
        User requester = null;

        // Check for the requesterUsername in the DB, if it does not exist assume it's a Non-Logged-in user
        if (requesterUsername != null) {
            requester = userRepository.findByUsername(requesterUsername).orElse(null);
        }

        List<Comment> comments = commentRepository.findAllByPostAndParentIsNull(post);

        final User finalRequester = requester;

        comments.forEach(comment -> {

            populateUserVoteStatus(comment, finalRequester);

            // Load the replies to prevent Lazy Loading from crashing the request
            comment.getReplies().size();
        });

        return comments;
    }

    @Transactional
    public Comment editComment(UUID commentId, String newText, String requesterUsername) {

        Comment comment = findCommentById(commentId, requesterUsername );

        if (comment.isDeleted()) {
            throw new IllegalStateException("Cannot edit a deleted comment");
        }

        User author = userRepository.findByUsername(requesterUsername)
                .orElseThrow(() -> new IllegalArgumentException("User " + requesterUsername + " user not found "));

        if (!Objects.equals(comment.getAuthor(), author)) {
            throw new IllegalArgumentException("You are not the author of this post");
        }

        comment.setContent(newText);
        comment.setUpdatedAt(Instant.now());
        commentRepository.save(comment);
        return comment;
    }

    @Transactional
    public void removeComment(UUID commentId, String requesterUsername) {
        // try to find this comment
        Comment comment = findCommentById(commentId,requesterUsername);

        if (comment.isDeleted()) {
            throw new IllegalStateException("Comment with id " + commentId + " is already deleted");
        }

        User author = userRepository.findByUsername(requesterUsername)
                .orElseThrow(() -> new IllegalStateException("Authenticated user not found"));

        if (!Objects.equals(comment.getAuthor(), author)) {
            throw new IllegalStateException("This comment was not created by " + author);
        }

        comment.setDeleted(true);
        commentRepository.save(comment);
    }

    @Transactional
    public Comment voteComment(UUID id, String voteType, String requesterUsername) {

        Comment comment = findCommentById(id,requesterUsername);

        if (comment.isDeleted()) {
            throw new IllegalStateException("Cannot vote on a deleted comment");
        }

        User requester = userRepository.findByUsername(requesterUsername)
                .orElseThrow(() -> new IllegalArgumentException("User " + requesterUsername + " not found"));

        CommentVote commentVote = commentVoteRepository.findByCommentAndAuthor(comment, requester).orElse(null);

        if (commentVote == null) {
            commentVote = new CommentVote();
            commentVote.setComment(comment);
            commentVote.setAuthor(requester);
        }

        VoteType currentVote = commentVote.getVoteType();

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
                commentVote.setVoteType(VoteType.UP);
            }
            case "down" -> {
                comment.setDownvotes(comment.getDownvotes() + 1);
                commentVote.setVoteType(VoteType.DOWN);
            }
            case "none" -> commentVote.setVoteType(null);
            case null, default -> throw new IllegalArgumentException("Invalid vote.");
        }

        comment.setScore(comment.getUpvotes() - comment.getDownvotes());

        if (commentVote.getVoteType() != null) {
            comment.setUserVote(commentVote.getVoteType().toString().toLowerCase());
        } else {
            comment.setUserVote(null);
        }

        comment.setUpdatedAt(Instant.now());

        commentVoteRepository.save(commentVote);
        commentRepository.save(comment);
        return comment;
    }
}

