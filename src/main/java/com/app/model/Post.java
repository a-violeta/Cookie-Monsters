package com.app.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;

@Data
@Entity
@Table(name = "posts")

public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private long communityId;
    private long userId;
    private String title;
    private String text;
    private LocalDateTime createdAt;

    @Transient
    Community community;
    @Transient
    User user;

    @Transient
    private Media media;
    @Transient
    private List<Comment> commentList;

    // validations not made in post constructors

    public Post() {
        this.communityId = 0;
        this.userId = 0;
        this.title = "";
        this.text = "";
        this.commentList = null;
        this.createdAt = LocalDateTime.now();
        this.media = null;
    }

    public Post(long communityId, long userId, String title, String text, List<Comment> commentList) {
        this.communityId = communityId;
        this.userId = userId;
        this.title = title;
        this.text = text;
        this.commentList = commentList;
        this.createdAt = LocalDateTime.now();
        this.media = null;
    }

    public Post(long communityId, long userId, String title, String text, List<Comment> commentList, LocalDateTime createdAt, Media media) {
        this.communityId = communityId;
        this.userId = userId;
        this.title = title;
        this.text = text;
        this.commentList = commentList;
        this.createdAt = createdAt;
        this.media = media;
    }

    public void addComment(Comment comment) {

        // moved all validations to services
        commentList.add(comment);
    }

    public void removeComment(long commentId) {
        Iterator<Comment> it = commentList.iterator();
        // removing from list by using iterator
        while (it.hasNext()) {
            Comment c = it.next();
            if (c.getCommentId() == commentId) {
                it.remove();
                break;
            }
        }
    }
}
