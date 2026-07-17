package com.app.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@RequiredArgsConstructor
public class Comment {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;
    @Setter
    private String text;
    private final long userId;
    private final long postId;
    private LocalDateTime createdAt;

    // validations not made here

    @Override
    public String toString() {
        return "Comment{" +
                "commentId=" + commentId +
                ", text='" + text + '\'' +
                ", userId=" + userId +
                ", postId=" + postId +
                ", createdAt=" + createdAt +
                '}';
    }
}
