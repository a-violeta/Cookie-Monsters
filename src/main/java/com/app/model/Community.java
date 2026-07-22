package com.app.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "communities")
public class Community {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    public void addPost(Post post){
        communityPosts.add(post);
    }

    public void removePost(long postId){
        Iterator<Post> it = communityPosts.iterator();
        // removing from list by using iterator
        while(it.hasNext()){
            Post p = it.next();
            if(p.getId() == postId){
                it.remove();
                break;
            }
        }
    }

    public Post findPostById(long postId){

        // if there are any posts at all, we search
        if(this.getCommunityPosts()!=null && !this.getCommunityPosts().isEmpty()) {
            for (Post p : this.getCommunityPosts()) {
                if(p.getId()==postId){
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
            if(u.getId() == userId){
                it.remove();
                break;
            }
        }
    }

    public User findUserById(long userId){

        // if there are any users at all, we search
        if(this.getCommunityUsers()!=null && !this.getCommunityUsers().isEmpty()) {
            for (User u : this.getCommunityUsers()) {
                if(u.getId()==userId){
                    return u;
                }
            }
        }
        return null;
    }
}
