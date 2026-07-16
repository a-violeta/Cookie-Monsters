package com.app.service;

import com.app.model.Post;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Getter
@Service
public class PostService {

    private List<Post> applicationPosts = new ArrayList<>();
    // list of all the posts

    public void validatePost(String title, String text) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title is required");
        }

        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("Text is required");
        }
    }

    public Post addPost(long communityId, String title, String text, long userId) {
        validatePost(title, text);
        Post post = new Post(communityId, userId, title, text, new ArrayList<>());
        this.applicationPosts.add(post);
        return post;
    }

    public Post findPostById(long postId) {
        for (Post post : applicationPosts) {
            if (post.getPostId() == postId) {
                return post;
            }
        }

        return null;
    }

    public void editPost(long postId, String newText) {
        Post post = findPostById(postId);

        if (post != null) {
            validatePost(post.getTitle(), newText);
            post.setText(newText);
        } else {
            throw new IllegalArgumentException("Post with id " + postId + " not found");
        }
    }

    public void removePost(long postId) {
        boolean found = false;
        Iterator<Post> it = applicationPosts.iterator();
        // removing from list by using iterator
        while (it.hasNext()) {
            Post post = it.next();
            if (post.getPostId() == postId) {
                found = true;
                it.remove();
                break;
            }
        }

        if (!found) {
            throw new IllegalArgumentException("Post with id " + postId + " not found");
        }
    }
}