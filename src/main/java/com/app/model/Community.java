package com.app.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"communityUsers", "communityPosts"})
@Entity
@Table(name = "communities")
public class Community {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long communityId;

    private String communityName;
    private String description;
    private LocalDateTime createdAt;

    @ManyToMany
    @JoinTable(
            name = "community_users",
            joinColumns = @JoinColumn(name = "community_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> communityUsers;

    @OneToMany(mappedBy = "community", cascade = CascadeType.ALL)
    private List<Post> communityPosts;

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

    /*
    this is dead code, may be useful if we move this logic to services

    public void addPost(Post post){
        post.setCommunity(this);
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
    */

}
