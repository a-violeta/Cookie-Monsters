package com.app.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"community", "user", "commentList", "media"})
@Entity
@Table(name = "posts")
public class Post {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @ManyToOne
    @JoinColumn(name = "community_id")
    private Community community;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String title;
    private String text;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comment> commentList;

    private LocalDateTime createdAt;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "media_id")
    private Media media;

    // validations not made in post constructors

    /*
    not sure these constructors are needed anymore

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
    */

    /*
    this is dead code, may be useful if we move this logic to services

    public void addComment(Comment comment){

        // moved all validations to services
        comment.setPost(this);
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
    */
}
