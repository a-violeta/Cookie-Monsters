package com.app.service;

import com.app.model.Post;

import java.util.List;

public interface PostUseCases {
    void validatePost(String title, String text);
    Post addPost(long communityId, long userId, String title, String text);
    void deletePost(long postId);
    List<Post> listPosts(long communityId);
    Post findPostById(long postId);
    void editPost(long postId, String newText);
}