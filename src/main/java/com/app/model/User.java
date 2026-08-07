package com.app.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
// @ToString(exclude = {"password", "communities", "posts", "comments"})
@Entity
@Table(name = "app_users") // user is a reserved name in postgres
public class User {

    // no id generator
    // let database make it for no errors in future
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id; // JPA needs to be null before using

    private String username;
    private String email;
    private String password;
    private String description;

    // New fields mapped from the API specification
    private String displayName;
    private String avatarUrl;

    private LocalDateTime createdAt;

    @ManyToMany(mappedBy = "communityUsers")
    private List<Community> communities;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    // cascade: whatever operation happens to a User, propagate that same operation to all the Posts in its posts list automatically
    // consequence: deleting a User also deletes all their Posts
    private List<Post> posts;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<Comment> comments;

    // constructors
    public User() {
        this.createdAt = LocalDateTime.now();
    }

    public User(String username, String email, String password, String description) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.description = description;
        this.createdAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", displayName='" + displayName + '\'' +
                ", description='" + description + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}