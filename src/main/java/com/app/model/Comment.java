package com.app.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Comment {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    private String text;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @Setter
    private LocalDateTime createdAt;

    @Override
    public String toString() {
        return "Comment{" +
                "commentId=" + id +
                ", text='" + text + '\'' +
                ", userId=" + user.getId() +
                ", postId=" + post.getId() +
                ", createdAt=" + createdAt +
                '}';
    }
}