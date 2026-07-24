package com.app.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;
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
    private Long id;

    @ManyToOne
    @JoinColumn(name = "community_id")
    private Community community;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String title;
    private String text;
    private LocalDateTime createdAt;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "media_id")
    private Media media;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comment> commentList;

    public Post() {
        this.community = null;
        this.user = null;
        this.title = "";
        this.text = "";
        this.commentList = null;
        this.createdAt = LocalDateTime.now();
        this.media = null;
    }

    public Post(Community community, User user, String title, String text, List<Comment> commentList, Media media) {
        this.community = community;
        this.user = user;
        this.title = title;
        this.text = text;
        this.commentList = commentList;
        this.createdAt = LocalDateTime.now();
        this.media = media;
    }

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
