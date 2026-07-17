package com.app.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Comment {

    private static long idIncrementor = 0;
    // for id uniqueness, ids given will be 1, then 2, 3 ...

    @EqualsAndHashCode.Include
    private final long commentId;
    @Setter
    private String text;
    private final long userId;
    private final long postId;
    private LocalDateTime createdAt;

    // validations not made here

    private static long incrementId(){
        idIncrementor++;
        return idIncrementor;
    }

    public Comment(){
        this.commentId = incrementId();
        this.text="";
        this.userId=0;
        this.postId=0;
        this.createdAt=LocalDateTime.now();
    }

    public Comment(String text, long userId, long postId){
        this.commentId=incrementId();
        this.text=text;
        this.userId=userId;
        this.postId=postId;
        this.createdAt=LocalDateTime.now();
    }

    public Comment(String text, long userId, long postId, LocalDateTime createdAt){
        this.commentId=incrementId();
        this.text=text;
        this.userId=userId;
        this.postId=postId;
        this.createdAt=createdAt;
    }

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
