package com.app.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Community {

    @EqualsAndHashCode.Include
    private static long idIncrementor = 0;
    // for id uniqueness, ids given will be 1, then 2, 3 ...

    private static long incrementId(){
        idIncrementor++;
        return idIncrementor;
    }

    private final long communityId;
    @Setter
    private String communityName;
    @Setter
    private String description;
    @Setter
    private List<User> communityUsers;
    @Setter
    private List<Post> communityPosts;
    private LocalDateTime createdAt;

    public Community(){
        this.communityId=incrementId();
        this.communityName="";
        this.description="";
        this.communityUsers=null;
        this.communityPosts=null;
        this.createdAt=LocalDateTime.now();
    }

    public Community(String communityName, String description, List<User> communityUsers, List<Post> communityPosts){
        this.communityId=incrementId();
        this.communityName=communityName;
        this.description=description;
        this.communityUsers=communityUsers;
        this.communityPosts=communityPosts;
        this.createdAt=LocalDateTime.now();
    }

    public Community(String communityName, String description, List<User> communityUsers, List<Post> communityPosts, LocalDateTime createdAt){
        this.communityId=incrementId();
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

    @Override
    public String toString() {
        return "Community{" +
                "communityId=" + communityId +
                ", communityName='" + communityName + '\'' +
                ", description='" + description + '\'' +
                ", communityUsers=" + communityUsers +
                ", communityPosts=" + communityPosts +
                ", createdAt=" + createdAt +
                '}';
    }
}
