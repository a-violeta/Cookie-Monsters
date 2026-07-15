package com.app.model;

import java.util.Iterator;
import java.util.List;

public class Post {

    private static long idIncrementor = 0;
    // for id uniqueness, ids given will be 1, then 2, 3 ...

    private long postId;
    private long communityId;
    private long userId;
    private String title;
    private String text;
    private List<Comment> commentList;
    // missing creatinDate

    // validations not made in post constructors

    public Post(){
        this.postId= incrementId();
        this.communityId=0;
        this.userId=0;
        this.title="";
        this.text="";
        this.commentList=null;
    }

    public Post(long communityId, long userId, String title, String text, List<Comment> commentList){
        this.postId=incrementId();
        this.communityId=communityId;
        this.userId=userId;
        this.title=title;
        this.text=text;
        this.commentList=commentList;
    }

    private static long incrementId(){
        idIncrementor++;
        return idIncrementor;
    }

    public long getCommunityId() {
        return communityId;
    }
    public List<Comment> getCommentList() {
        return commentList;
    }
    public long getPostId() {
        return postId;
    }
    public long getUserId() {
        return userId;
    }
    public String getText() {
        return text;
    }
    public String getTitle() {
        return title;
    }

    public void setText(String text) {
        this.text = text;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }
    public void setUserId(long userId) {
        this.userId = userId;
    }
    public void setCommunityId(long communityId) {
        this.communityId = communityId;
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
                '}';
    }

    public void addComment(Comment comment){
        if(!comment.getText().isEmpty() && comment.getPostId()!=this.getPostId() && comment.getUserId()!=0)
            // validates text not empty, post is the same, user is not nonexistent

            commentList.add(comment);
        else
            System.out.println("Cannot add invalid comment! Check comment text, user and post");
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

    @Override
    public int hashCode() {
        return Long.hashCode(postId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Post)) {
            return false;
        }

        Post other = (Post) obj;
        return postId == other.postId;
    }
}
