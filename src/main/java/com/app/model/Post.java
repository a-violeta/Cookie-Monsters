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
@ToString(exclude = {"subreddit", "author", "commentList", "media"})
@Entity
@Table(name = "posts")
public class Post {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "community_id")
    private Community subreddit;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User author;

    private String title;
    private String content;
    private LocalDateTime createdAt;

    @OneToOne(cascade = CascadeType.ALL)
    // cascade: whatever operation happens to a Post, propagate that same operation to the Media automatically
    // consequence: deleting a Post also deletes the Media
    @JoinColumn(name = "media_id")
    private Media media;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comment> commentList;

    public Post() {
        this.subreddit = null;
        this.author = null;
        this.title = "";
        this.content = "";
        this.commentList = null;
        this.createdAt = LocalDateTime.now();
        this.media = null;
    }

    public Post(Community subreddit, User author, String title, String content, List<Comment> commentList, Media media) {
        this.subreddit = subreddit;
        this.author = author;
        this.title = title;
        this.content = content;
        this.commentList = commentList;
        this.createdAt = LocalDateTime.now();
        this.media = media;
    }
}
