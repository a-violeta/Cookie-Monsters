package com.app.service;

import com.app.model.Post;

public interface PostUseCases {
    void validatePost(String title, String text);
    Post addPost(long communityId, String title, String text, long userId);
    Post findPostById(long postId);
    void editPost(long postId, String newText);
    void removePost(long postId);
}
