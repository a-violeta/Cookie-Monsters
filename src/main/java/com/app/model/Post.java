package com.app.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"subreddit", "author", "commentList", "media"})
@Entity
@Table(name = "posts")
@SQLDelete(sql = "UPDATE posts SET is_deleted = true WHERE id=?")
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

    @Column(length = 300)
    private String title;
    @Column(length = 10000)
    private String content;
    private Instant createdAt;
    private Instant updatedAt;

    private long upvotes;
    private long downvotes;
    private long score;

    private long commentCount;

    // soft delete: same convention as User - keeps replies structurally intact
    // (avoids the parent_id FK constraint failure a hard delete hits) and matches
    // real Reddit behavior (content masked to "[deleted]", thread stays in place)
    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean isDeleted = false;

    @Transient
    private String userVote;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    // PERSIST/MERGE only - no REMOVE. A new Media still gets saved together with the
    // Post, but soft-deleting the Post (@SQLDelete -> UPDATE) no longer cascades a real
    // DELETE to the Media row. That hard delete was the bug: the posts row survives
    // (soft-deleted) and still references media_id, so removing the Media row broke the
    // foreign key. The mapper already hides the image once the post is deleted
    // (imageUrlOrDeleted), so leaving the row in place is correct.
    @JoinColumn(name = "media_id")
    private Media media;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<PostVote> votes;

    // No cascade. Comments are created/persisted independently through
    // CommentRepository/CommentService, so PERSIST/MERGE aren't needed here - and
    // REMOVE must never cascade: soft-deleting a Post should not touch its comments
    // at all. Reddit keeps replies visible under a deleted post; it doesn't delete them.
    @OneToMany(mappedBy = "post")
    private List<Comment> commentList;

    public Post() {
        this.subreddit = null;
        this.author = null;
        this.title = "";
        this.content = "";
        this.commentList = null;
        this.createdAt = Instant.now();
        this.media = null;
    }

    public Post(Community subreddit, User author, String title, String content, List<Comment> commentList, Media media) {
        this.subreddit = subreddit;
        this.author = author;
        this.title = title;
        this.content = content;
        this.commentList = commentList;
        this.createdAt = Instant.now();
        this.media = media;
    }
}