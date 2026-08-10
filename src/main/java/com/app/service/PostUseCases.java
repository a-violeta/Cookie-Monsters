package com.app.service;

import com.app.model.Post;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface PostUseCases {
    void validatePost(String title, String content);
    Post addPost(String title, String content, String subreddit, String requesterUsername,
                 MultipartFile image, Integer filter);
    void deletePost(UUID postId, String requesterUsername);
    List<Post> listPosts(UUID communityId);
    List<Post> listPosts(String requesterUsername);
    Post findPostById(UUID postId, String requesterUsername);
    Post editPost(UUID postId, String newTitle, String newContent, String requesterUsername);
    Post votePost(UUID postId, String voteType,  String requesterUsername);
    List<Post> listPostsBySubreddit(String subreddit, String requesterUsername);
}