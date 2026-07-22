package com.app.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
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

    @Setter
    private String title;
    @Setter
    private String text;

    @Setter
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comment> commentList;

    @Setter
    private LocalDateTime createdAt;

    @Setter
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "media_id")
    private Media media;

    // validations not made in post constructors

    @Override
    public String toString() {
        return "Post{" +
                "postId=" + id +
                ", communityId=" + community.getId() +
                ", userId=" + user.getId() +
                ", title='" + title + '\'' +
                ", text='" + text + '\'' +
                ", commentList=" + commentList +
                ", createdAt=" + createdAt +
                ", media=" + media +
                '}';
    }

    public void addComment(Comment comment){

        // moved all validations to services
        commentList.add(comment);
    }

    public void removeComment(long commentId){
        Iterator<Comment> it = commentList.iterator();
        // removing from list by using iterator
        while(it.hasNext()){
            Comment c = it.next();
            if(c.getId() == commentId){
                it.remove();
                break;
            }
        }
    }
}
