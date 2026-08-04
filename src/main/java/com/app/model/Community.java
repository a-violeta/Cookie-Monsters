package com.app.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"communityUsers", "communityPosts"})
@Entity
@Table(name = "communities")
public class Community {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;
    private String displayName;
    private String description;
    private LocalDateTime createdAt;
    private String iconUrl;

    @ManyToMany
    @JoinTable(
            name = "community_users",
            joinColumns = @JoinColumn(name = "community_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> communityUsers;

    @OneToMany(mappedBy = "community", cascade = CascadeType.ALL)
    // cascade: whatever operation happens to a Community, propagate that same operation to all the Posts in its posts list automatically
    // consequence: deleting a Community also deletes all their Posts
    private List<Post> communityPosts;

    public Community() {
        this.name = "";
        this.displayName = "";
        this.description = "";
        this.createdAt = LocalDateTime.now();
        this.iconUrl = null;
        this.communityUsers = null;
        this.communityPosts = null;
    }

    public Community(String communityName, String displayName, String description, String iconUrl, List<User> communityUsers, List<Post> communityPosts) {
        this.name = communityName;
        this.displayName = displayName;
        this.description = description;
        this.createdAt = LocalDateTime.now();
        this.iconUrl = iconUrl;
        this.communityUsers = communityUsers;
        this.communityPosts = communityPosts;
    }

    public Post findPostById(long postId){

        // if there are any posts at all, we search
        if (this.getCommunityPosts() != null && !this.getCommunityPosts().isEmpty()) {
            for (Post p : this.getCommunityPosts()) {
                if (p.getId() == postId) {
                    return p;
                }
            }
        }
        return null;
    }

    public User findUserById(long userId){

        // if there are any users at all, we search
        if (this.getCommunityUsers() != null && !this.getCommunityUsers().isEmpty()) {
            for (User u : this.getCommunityUsers()) {
                if (u.getId() == userId) {
                    return u;
                }
            }
        }
        return null;
    }

}
