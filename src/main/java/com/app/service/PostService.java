package com.app.service;

import com.app.model.Community;
import com.app.model.Post;
import com.app.model.User;
import com.app.repository.CommunityRepository;
import com.app.repository.UserRepository;
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
public class PostService implements PostUseCases{

    private final PostRepository postRepository;
    private final CommunityRepository communityRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public void validatePost(String title, String text) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title is required");
        }

        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("Text is required");
        }
    }

    @Transactional
    public Post addPost(UUID communityId, long userId, String title, String text) {
        validatePost(title, text);

        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new IllegalArgumentException("Community with id " + communityId + " not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with id " + userId + " not found"));

        if (community.findUserById(userId) == null) {
            throw new IllegalArgumentException("User is not a member of this community");
        }

        Post post = new Post();

        post.setCommunity(community);
        post.setUser(user);
        post.setTitle(title);
        post.setText(text);
        post.setCreatedAt(LocalDateTime.now());
        post.setCommentList(new ArrayList<>());

        return postRepository.save(post);
    }

    @Transactional(readOnly = true)
    public Post findPostById(long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Post with id " + postId + " not found"
                        ));
    }

    @Transactional
    public List<Post> listPosts(UUID communityId) {
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new IllegalArgumentException("Community with id " + communityId + " not found"));

        List<Post> posts = new ArrayList<>();
        for (Post post : postRepository.findAll()) {
            if (Objects.equals(post.getCommunity(), community)) {
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
    public void editPost(long postId, String newText) {
        Post post = findPostById(postId);

        if (!Objects.equals(post.getUser(), userService.getLoggedInUser())) {
            throw new IllegalArgumentException("You are not the author of this post");
        }

        validatePost(post.getTitle(), newText);
        post.setText(newText);
        postRepository.save(post);
    }

    public void deletePost(long postId) {
        Post post = findPostById(postId);

        if (!Objects.equals(post.getUser(), userService.getLoggedInUser())) {
            throw new IllegalArgumentException("You are not the author of this post");
        }

        postRepository.delete(post);
    }
}