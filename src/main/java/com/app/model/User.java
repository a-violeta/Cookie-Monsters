package com.app.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"password", "communities", "posts", "comments"})
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

    // new fields mapped from the API specification
    private String displayName;
    private String avatarUrl;

    /*
    soft delete: keeps posts/comments intact and keeps the username permanently
    taken (existsByUsername already checks the whole table, deleted rows included)

    columnDefinition is required here: without a DEFAULT, Hibernate's ddl-auto=update
    generates "ALTER TABLE users ADD COLUMN is_deleted BOOLEAN NOT NULL" with no
    fallback value - which Postgres rejects outright on a table that already has rows
    (it has nothing to backfill existing rows with). Hibernate just logs that failure
    and starts the app anyway, so the column silently never gets added, and the very
    next INSERT references a column that doesn't exist -> raw 500.

     */
    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean isDeleted = false;

    private Instant createdAt;

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
        this.createdAt = Instant.now();
    }

    public User(String username, String email, String password, String description) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.description = description;
        this.createdAt = Instant.now();
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