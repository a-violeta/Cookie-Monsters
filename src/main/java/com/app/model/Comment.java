package com.app.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.EqualsAndHashCode;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"author", "post","parent"})
@Entity
@Table(name = "comments")
public class Comment {

    @Id // PK of the table
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @EqualsAndHashCode.Exclude
    private Comment parent;

    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User author;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    private Instant createdAt;

    private Instant updatedAt;

    private long upvotes;

    private long downvotes;

    private long score;

    @Transient
    private String userVote;

    @OneToMany(mappedBy = "parent")
    private List<Comment> replies;

    public Comment(){
        this.content = "";
        this.author = null;
        this.post = null;
        this.createdAt = Instant.now();
        // lombok annotation @NoArgsConstructor would make createdAt = null
        // I think it s better to use current time though
    }

    public Comment(String content, User user, Post post){
        this.content = content;
        this.author = user;
        this.post = post;
        this.createdAt = Instant.now();
    }
}