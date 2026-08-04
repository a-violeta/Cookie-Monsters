package com.app.service;

import com.app.model.*;
import com.app.repository.CommunityRepository;
import com.app.repository.UserRepository;
import com.app.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import com.app.repository.PostRepository;
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
    private final UserService userService;
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
    public Post addPost(long communityId, long userId, String title, String content) {
        validatePost(title, content);

        Community subreddit = communityRepository.findById(communityId)
                .orElseThrow(() -> new IllegalArgumentException("Community with id " + communityId + " not found"));

        User author = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with id " + userId + " not found"));

        if (subreddit.findUserById(userId) == null) {
            throw new IllegalArgumentException("You are not a member of this community");
        }

        Post post = new Post();

        post.setSubreddit(subreddit);
        post.setAuthor(author);
        post.setTitle(title);
        post.setContent(content);
        post.setCreatedAt(LocalDateTime.now());
        post.setCommentList(new ArrayList<>());

        post.setUpvotes(1);
        post.setScore(1);
        post.setUserVote("up");

        postRepository.save(post);

        Vote vote = new Vote();
        vote.setAuthor(author);
        vote.setPost(post);
        vote.setUserVote(VoteType.UP);
        voteRepository.save(vote);

        return post;
    }

    @Transactional(readOnly = true)
    public Post findPostById(UUID postId) {
        return postRepository.findById(postId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Post with id " + postId + " not found"
                        ));
    }

    @Transactional
    public List<Post> listPosts(long communityId) {
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
    public List<Post> listPosts() {
        return postRepository.findAll();
    }

    @Transactional
    public void editPost(UUID postId, String newContent) {
        Post post = findPostById(postId);

        if (!Objects.equals(post.getAuthor(), userService.getLoggedInUser())) {
            throw new IllegalArgumentException("You are not the author of this post");
        }

        validatePost(post.getTitle(), newContent);
        post.setContent(newContent);
        postRepository.save(post);
    }

    public void deletePost(UUID postId) {
        Post post = findPostById(postId);

        if (!Objects.equals(post.getAuthor(), userService.getLoggedInUser())) {
            throw new IllegalArgumentException("You are not the author of this post");
        }

        postRepository.delete(post);
    }

    @Transactional
    public Post votePost(UUID postId, String voteType) {
        Post post = findPostById(postId);
        User currentUser = userService.getLoggedInUser();
        Vote vote = voteRepository.findByPostAndAuthor(post, currentUser).orElse(null);

        if (vote == null) {
            vote = new Vote();
            vote.setPost(post);
            vote.setAuthor(currentUser);
        } else {
            if (vote.getUserVote() == VoteType.UP) {
                post.setUpvotes(post.getUpvotes() - 1);
            }
            if (vote.getUserVote() == VoteType.DOWN) {
                post.setDownvotes(post.getDownvotes() - 1);
            }
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

        voteRepository.save(vote);
        postRepository.save(post);
        return post;
    }
}