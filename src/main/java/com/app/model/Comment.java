package com.app.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"user", "post"})
@Entity
@Table(name = "comments")
public class Comment {

    @Id // PK of the table
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    private String text;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    private LocalDateTime createdAt;

    public Comment(){
        this.text = "";
        this.user = null;
        this.post = null;
        this.createdAt = LocalDateTime.now();
        // lombok annotation @NoArgsConstructor would make createdAt = null
        // I think it s better to use current time though
    }

    public Comment(String text, User user, Post post){
        this.text = text;
        this.user = user;
        this.post = post;
        this.createdAt = LocalDateTime.now();
    }
}