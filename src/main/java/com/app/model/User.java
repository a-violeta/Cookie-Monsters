package com.app.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "app_users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

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
    private List<Post> posts;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Comment> comments;

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
                ", createdAt=" + createdAt +
                '}';
    }
}