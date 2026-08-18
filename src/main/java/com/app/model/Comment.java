package com.app.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.EqualsAndHashCode;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
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

    @Column(length = 1000)
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

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean isDeleted = false;

    @Transient
    private String userVote;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> replies;

    public Comment(){
        this.content = "";
        this.author = null;
        this.post = null;
        this.createdAt = Instant.now();
        // lombok annotation @NoArgsConstructor would make createdAt = null
        // I think it is better to use current time though
    }

    public Comment(String content, User user, Post post){
        this.content = content;
        this.author = user;
        this.post = post;
        this.createdAt = Instant.now();
    }
}