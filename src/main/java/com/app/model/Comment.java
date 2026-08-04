package com.app.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"user", "post"})
@Entity
@Table(name = "comments")
public class Comment {

    @Id // PK of the table
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    private LocalDateTime createdAt;

    private LocalDateTime UpdatedAt;

    private LocalDateTime DeletedAt;

    public Comment(){
        this.content = "";
        this.user = null;
        this.post = null;
        this.createdAt = LocalDateTime.now();
        // lombok annotation @NoArgsConstructor would make createdAt = null
        // I think it s better to use current time though
    }

    public Comment(String content, User user, Post post){
        this.content = content;
        this.user = user;
        this.post = post;
        this.createdAt = LocalDateTime.now();
    }
}