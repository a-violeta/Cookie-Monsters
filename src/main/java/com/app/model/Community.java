package com.app.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;

@Data
@Entity
@Table(name = "communities")

public class Community {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private final String communityName;
    private String description;
    private final LocalDateTime createdAt;

    @Transient
    private List<User> communityUsers;
    @Transient
    private List<Post> communityPosts;
    // we need to specify a relationship between these
    // otherwise Hibernate thinks Post and User are basic types (like String)
    // and that fails

    // Transient basically says to ignore these fields for now
    // because User and Post and Comment are not yet finished

    public Community(){
        this.communityName="";
        this.description="";
        this.communityUsers=null;
        this.communityPosts=null;
        this.createdAt=LocalDateTime.now();
    }

    public Community(String communityName, String description, List<User> communityUsers, List<Post> communityPosts){
        this.communityName=communityName;
        this.description=description;
        this.communityUsers=communityUsers;
        this.communityPosts=communityPosts;
        this.createdAt=LocalDateTime.now();
    }

    public Community(String communityName, String description, List<User> communityUsers, List<Post> communityPosts, LocalDateTime createdAt){
        this.communityName=communityName;
        this.description=description;
        this.communityUsers=communityUsers;
        this.communityPosts=communityPosts;
        this.createdAt=createdAt;
    }

    public void addPost(Post post){
        communityPosts.add(post);
    }

    public void removePost(long postId){
        Iterator<Post> it = communityPosts.iterator();
        // removing from list by using iterator
        while(it.hasNext()){
            Post p = it.next();
            if(p.getPostId() == postId){
                it.remove();
                break;
            }
        }
    }

    public Post findPostById(long postId){

        // if there are any posts at all, we search
        if(this.getCommunityPosts()!=null && !this.getCommunityPosts().isEmpty()) {
            for (Post p : this.getCommunityPosts()) {
                if(p.getPostId()==postId){
                    return p;
                }
            }
        }
        return null;
    }

    public void addUser(User user){
        communityUsers.add(user);
    }

    public void removeUser(long userId){
        Iterator<User> it = communityUsers.iterator();
        // removing from list by using iterator
        while(it.hasNext()){
            User u = it.next();
            if(u.getUserId() == userId){
                it.remove();
                break;
            }
        }
    }

    public User findUserById(long userId){

        // if there are any users at all, we search
        if(this.getCommunityUsers()!=null && !this.getCommunityUsers().isEmpty()) {
            for (User u : this.getCommunityUsers()) {
                if(u.getUserId()==userId){
                    return u;
                }
            }
        }
        return null;
    }
}
