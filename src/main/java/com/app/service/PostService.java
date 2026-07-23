package com.app.service;

import com.app.model.Community;
import com.app.model.Post;
import com.app.model.User;
import com.app.repository.CommunityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService implements  PostUseCases{

    private final PostRepository postRepository;
    private final CommunityRepository communityRepository;
    private final UserRepository userRepository;

    public void validatePost(String title, String text) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title is required");
        }

        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("Text is required");
        }
    }

    @Transactional
    public Post addPost(long communityId, String title, String text, long userId) {
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
                .orElseThrow(() -> new IllegalArgumentException("Post with id " + postId + " not found"));
    }

    @Transactional(readOnly = true)
    public List<Post> listPosts() {
        return postRepository.findAll();
    }

    @Transactional
    public void editPost(long postId, String newText) {
        Post post = findPostById(postId);

        if (post != null) {
            validatePost(post.getTitle(), newText);
            post.setText(newText);
            postRepository.save(post);
        } else {
            throw new IllegalArgumentException("Post with id " + postId + " not found");
        }
    }

    public void removePost(long postId) {
        Post post = findPostById(postId);
        postRepository.delete(post);
    }
}