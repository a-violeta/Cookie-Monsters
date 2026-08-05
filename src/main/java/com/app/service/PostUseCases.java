package com.app.service;

import com.app.model.Post;

import java.util.List;
import java.util.UUID;

public interface PostUseCases {
    void validatePost(String title, String content);
    Post addPost(UUID communityId, long userId, String title, String content);
    void deletePost(UUID postId);
    List<Post> listPosts(UUID communityId);
    List<Post> listPosts();
    Post findPostById(UUID postId);
    void editPost(UUID postId, String newContent);
    Post votePost(UUID postId, String voteType);
}