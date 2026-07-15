package com.app.service;

// singleton class, there is no need for more than 1 entity that manages posts

import com.app.model.Comment;
import com.app.model.Post;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Getter
@Service
public class PostService {

    private List<Post> applicationPosts;
    // list of all the posts

    //private constructor for singleton design pattern
    private PostService() {
        this.applicationPosts = new ArrayList<>();
    }

    // holder implementation for singleton
    // so we call method getInstance() once and it spawns 1 obj
    // if we call the method again it returns the same obj made earlier
    private static class Holder {
        private static final PostService INSTANCE = new PostService();
    }

    public static PostService getInstance() {
        return PostService.Holder.INSTANCE;
    }

    public void addCommentToPost(Post post, Comment comment){
        if(!comment.getText().isEmpty()
                && comment.getPostId()!=post.getPostId()
                && comment.getUserId()!=0)
                // validates text not empty, post is the same, user is not nonexistent

            post.addComment(comment);
        else
            System.out.println("Cannot add invalid comment! Check comment text, user and post");
    }
}