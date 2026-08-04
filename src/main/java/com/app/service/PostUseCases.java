package com.app.service;

import com.app.model.Post;

import java.util.List;
import java.util.UUID;

public interface PostUseCases {
    void validatePost(String title, String text);
    Post addPost(UUID communityId, long userId, String title, String text);
    void deletePost(long postId);
    List<Post> listPosts(UUID communityId);
    List<Post> listPosts();
    Post findPostById(long postId);
    void editPost(long postId, String newText);
}