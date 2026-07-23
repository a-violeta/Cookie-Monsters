package com.app.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "comments")
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Comment {

    @Id // PK of the table
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long commentId;

    private String text;
    private long userId;
    private long postId;
    private LocalDateTime createdAt;

    public Comment(String text, long userId, long postId){
        this.text=text;
        this.userId=userId;
        this.postId=postId;
        this.createdAt=LocalDateTime.now();
    }

    public Comment(String text, long userId, long postId, LocalDateTime createdAt){
        this.text=text;
        this.userId=userId;
        this.postId=postId;
        this.createdAt=createdAt;
    }
}