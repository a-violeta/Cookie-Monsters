package com.app.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Post {

    @EqualsAndHashCode.Include
    private static long idIncrementor = 0;
    // for id uniqueness, ids given will be 1, then 2, 3 ...

    private final long postId;
    private final long communityId;
    private final long userId;
    @Setter
    private String title;
    @Setter
    private String text;
    @Setter
    private List<Comment> commentList;
    private LocalDateTime createdAt;

    // validations not made in post constructors

    public Post(){
        this.postId= incrementId();
        this.communityId=0;
        this.userId=0;
        this.title="";
        this.text="";
        this.commentList=null;
        this.createdAt=LocalDateTime.now();
    }

    public Post(long communityId, long userId, String title, String text, List<Comment> commentList){
        this.postId=incrementId();
        this.communityId=communityId;
        this.userId=userId;
        this.title=title;
        this.text=text;
        this.commentList=commentList;
    }

    public Post(long communityId, long userId, String title, String text, List<Comment> commentList, LocalDateTime createdAt){
        this.postId=incrementId();
        this.communityId=communityId;
        this.userId=userId;
        this.title=title;
        this.text=text;
        this.commentList=commentList;
        this.createdAt=createdAt;
    }

    private static long incrementId(){
        idIncrementor++;
        return idIncrementor;
    }

    @Override
    public String toString() {
        return "Post{" +
                "postId=" + postId +
                ", communityId=" + communityId +
                ", userId=" + userId +
                ", title='" + title + '\'' +
                ", text='" + text + '\'' +
                ", commentList=" + commentList +
                ", createdAt=" + createdAt +
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
            if(c.getCommentId() == commentId){
                it.remove();
                break;
            }
        }
    }
}
