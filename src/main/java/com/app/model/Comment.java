package com.app.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.SQLDelete;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"author", "post","parent"})
@Entity
@Table(name = "comments")
@SQLDelete(sql = "UPDATE comments SET is_deleted = true WHERE id=?")
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

    // soft delete: avoids the parent_id FK constraint failure a hard delete hits
    // when a comment has replies - content gets masked to "[deleted]" instead,
    // and the comment stays in place so replies underneath aren't orphaned
    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean isDeleted = false;

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