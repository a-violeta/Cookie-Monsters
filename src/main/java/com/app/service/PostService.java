package com.app.service;

import com.app.model.*;
import com.app.repository.CommunityRepository;
import com.app.repository.PostRepository;
import com.app.repository.UserRepository;
import com.app.repository.VoteRepository;
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
public class PostService implements PostUseCases {

    private final PostRepository postRepository;
    private final CommunityRepository communityRepository;
    private final UserRepository userRepository;
    private final VoteRepository voteRepository;

    public void validatePost(String title, String content) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title is required");
        }

        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("Content is required");
        }
    }

    @Transactional
    public Post addPost(String title, String content, String subredditName, String requesterUsername) {
        Community subreddit = communityRepository.findByName(subredditName)
                .orElseThrow(() -> new IllegalArgumentException("Subreddit " + subredditName + " not found"));

        User requester = userRepository.findByUsername(requesterUsername)
                .orElseThrow(() -> new IllegalArgumentException("User " + requesterUsername + " not found"));

        /*
        allow posting if you are not a member of the community
        because there is no option to join communities yet

        if (subreddit.findUserById(requester.getId()) == null) {
            throw new IllegalArgumentException("You are not a member of this community");
        }
        */

        Post post = new Post();

        post.setSubreddit(subreddit);
        post.setAuthor(requester);
        post.setTitle(title);
        post.setContent(content);
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());
        post.setCommentList(new ArrayList<>());

        // Voting initialization from the main branch
        post.setUpvotes(1);
        post.setScore(1);

        post.setCommentCount(post.getCommentList().size());

        post.setUserVote("up");

        postRepository.save(post);

        Vote vote = new Vote();
        vote.setAuthor(requester);
        vote.setPost(post);
        vote.setUserVote(VoteType.UP);
        voteRepository.save(vote);

        return post;
    }

    private void populateUserVoteStatus(Post post, User currentUser) {
        if (currentUser == null) {
            return;
        }

        voteRepository.findByPostAndAuthor(post, currentUser).ifPresent(vote -> {
            if (vote.getUserVote() != null) {
                post.setUserVote(vote.getUserVote().toString().toLowerCase());
            }
        });
    }

    @Transactional(readOnly = true)
    public Post findPostById(UUID postId, String requesterUsername) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Post with id " + postId + " not found"
                        ));

        User requester = userRepository.findByUsername(requesterUsername)
                .orElseThrow(() -> new IllegalArgumentException("User " + requesterUsername + " not found"));

        populateUserVoteStatus(post, requester);
        return post;
    }

    @Transactional(readOnly = true)
    public List<Post> listPosts(UUID communityId) {
        Community subreddit = communityRepository.findById(communityId)
                .orElseThrow(() -> new IllegalArgumentException("Community with id " + communityId + " not found"));

        List<Post> posts = new ArrayList<>();
        for (Post post : postRepository.findAll()) {
            if (Objects.equals(post.getSubreddit(), subreddit)) {
                posts.add(post);
            }
        }
        return posts;
    }

    @Transactional(readOnly = true)
    public List<Post> listPosts(String requesterUsername) {
        List<Post> posts = postRepository.findAll();
        User requester = userRepository.findByUsername(requesterUsername)
                .orElseThrow(() -> new IllegalArgumentException("User " + requesterUsername + " not found"));

        posts.forEach(post -> populateUserVoteStatus(post, requester));
        return posts;
    }

    @Transactional
    public Post editPost(UUID postId, String newTitle, String newContent, String requesterUsername) {
        if (newTitle == null && newContent == null) {
            throw new IllegalArgumentException("At least one field must be provided to update the post");
        }

        Post post = findPostById(postId, requesterUsername);

        User requester = userRepository.findByUsername(requesterUsername)
                .orElseThrow(() -> new IllegalArgumentException("User " + requesterUsername + " not found"));

        if (!Objects.equals(post.getAuthor(), requester)) {
            throw new IllegalArgumentException("You are not the author of this post");
        }

        if (newTitle != null) {
            post.setTitle(newTitle);
        }

        if (newContent != null) {
            post.setContent(newContent);
        }

        post.setUpdatedAt(LocalDateTime.now());
        postRepository.save(post);
        return post;
    }

    @Transactional
    public void deletePost(UUID postId, String requesterUsername) {
        Post post = findPostById(postId, requesterUsername);

        User requester = userRepository.findByUsername(requesterUsername)
                .orElseThrow(() -> new IllegalArgumentException("User " + requesterUsername + " not found"));

        if (!Objects.equals(post.getAuthor(), requester)) {
            throw new IllegalArgumentException("You are not the author of this post");
        }

        postRepository.delete(post);
    }

    @Transactional
    public Post votePost(UUID postId, String voteType, String requesterUsername) {
        Post post = findPostById(postId, requesterUsername);

        User requester = userRepository.findByUsername(requesterUsername)
                .orElseThrow(() -> new IllegalArgumentException("User " + requesterUsername + " not found"));

        Vote vote = voteRepository.findByPostAndAuthor(post, requester).orElse(null);

        if (vote == null) {
            vote = new Vote();
            vote.setPost(post);
            vote.setAuthor(requester);
        }

        VoteType currentVote = vote.getUserVote();

        // toggle logic: if voteType in request is the same as current vote, user intention is to cancel the vote
        if (("up".equals(voteType) && currentVote == VoteType.UP) ||
                ("down".equals(voteType) && currentVote == VoteType.DOWN)) {
            voteType = "none";
        }

        if (currentVote == VoteType.UP) {
            post.setUpvotes(post.getUpvotes() - 1);
        } else if (currentVote == VoteType.DOWN) {
            post.setDownvotes(post.getDownvotes() - 1);
        }

        switch (voteType) {
            case "up" -> {
                post.setUpvotes(post.getUpvotes() + 1);
                vote.setUserVote(VoteType.UP);
            }
            case "down" -> {
                post.setDownvotes(post.getDownvotes() + 1);
                vote.setUserVote(VoteType.DOWN);
            }
            case "none" -> vote.setUserVote(null);
            case null, default -> throw new IllegalArgumentException("Invalid vote.");
        }

        post.setScore(post.getUpvotes() - post.getDownvotes());

        if (vote.getUserVote() != null) {
            post.setUserVote(vote.getUserVote().toString().toLowerCase());
        } else {
            post.setUserVote(null);
        }

        post.setUpdatedAt(LocalDateTime.now());

        voteRepository.save(vote);
        postRepository.save(post);
        return post;
    }

    @Transactional(readOnly = true)
    public List<Post> listPostsBySubreddit(String subredditName, String requesterUsername) {
        List<Post> posts = postRepository.findBySubredditName(subredditName);
        User requester = userRepository.findByUsername(requesterUsername)
                .orElseThrow(() -> new IllegalArgumentException("User " + requesterUsername + " not found"));

        posts.forEach(post -> populateUserVoteStatus(post, requester));

        return posts;
    }
}