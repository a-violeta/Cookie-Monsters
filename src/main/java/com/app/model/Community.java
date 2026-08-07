package com.app.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.Instant;
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
    private Instant createdAt;
    private String iconUrl;

    @ManyToMany
    @JoinTable(
            name = "community_users",
            joinColumns = @JoinColumn(name = "community_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> communityUsers;

    // Fixed mappedBy to match the exact field name 'subreddit' from the Post entity
    @OneToMany(mappedBy = "subreddit", cascade = CascadeType.ALL)
    // cascade: whatever operation happens to a Community, propagate that same operation to all the Posts in its posts list automatically
    // consequence: deleting a Community also deletes all their Posts
    private List<Post> communityPosts;

    public Community() {
        this.name = "";
        this.displayName = "";
        this.description = "";
        this.createdAt = Instant.now();
        this.iconUrl = null;
        this.communityUsers = null;
        this.communityPosts = null;
    }

    public Community(String communityName, String displayName, String description, String iconUrl, List<User> communityUsers, List<Post> communityPosts) {
        this.name = communityName;
        this.displayName = displayName;
        this.description = description;
        this.createdAt = Instant.now();
        this.iconUrl = iconUrl;
        this.communityUsers = communityUsers;
        this.communityPosts = communityPosts;
    }

    // Changed long to UUID to match Post ID type
    public Post findPostById(UUID postId) {
        // if there are any posts at all, we search
        if (this.getCommunityPosts() != null && !this.getCommunityPosts().isEmpty()) {
            for (Post p : this.getCommunityPosts()) {
                if (p.getId().equals(postId)) { // Proper object comparison for UUID
                    return p;
                }
            }
        }
        return null;
    }

    // Changed parameter to Long object wrapper to use .equals() safely
    public User findUserById(Long userId) {
        // if there are any users at all, we search
        if (this.getCommunityUsers() != null && !this.getCommunityUsers().isEmpty()) {
            for (User u : this.getCommunityUsers()) {
                if (u.getId().equals(userId)) { // Proper object comparison
                    return u;
                }
            }
        }
        return null;
    }
}