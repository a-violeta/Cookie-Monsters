package com.app.service;

import com.app.model.Community;
import com.app.model.Post;
import com.app.repository.CommunityRepository;
import com.app.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PostService implements PostUseCases{

    private final PostRepository postRepository;
    private final CommunityRepository communityRepository;
//    private final UserRepository userRepository;

    public void validatePost(String title, String text) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title is required");
        }

        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("Text is required");
        }
    }

    public Post addPost(long communityId, long userId, String title, String text) {
        validatePost(title, text);

        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new IllegalArgumentException("Community with id " + communityId + " not found"));

//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new IllegalArgumentException("User with id " + userId + " not found"));

        Post post = new Post(communityId, userId, title, text, new ArrayList<>());
        post.setId(null);
        post.setCommunity(community);
//        post.setUser(user);

        return postRepository.save(post);
    }

    public Post findPostById(long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Post with id " + postId + " not found"
                        ));
    }

    public List<Post> listPosts(long communityId) {
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

    public void editPost(long postId, String newText) {
        Post post = findPostById(postId);
        validatePost(post.getTitle(), newText);
        post.setText(newText);
        postRepository.save(post);
    }

    public void deletePost(long postId) {
        Post post = findPostById(postId);
        postRepository.delete(post);
    }
}