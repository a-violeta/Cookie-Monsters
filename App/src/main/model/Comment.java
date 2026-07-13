package main.model;

public class Comment {

    private static long idIncrementor = 0;
    // for id uniqueness, ids given will be 1, then 2, 3 ...

    private long commentId;
    private String text;
    private long userId;
    private long postId;
    // missing date for now

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
    }

    public Comment(String text, long userId, long postId){
        this.commentId=incrementId();
        this.text=text;
        this.userId=userId;
        this.postId=postId;
    }

    public long getUserId() {
        return userId;
    }
    public long getPostId() {
        return postId;
    }
    public String getText() {
        return text;
    }
    public long getCommentId() {
        return commentId;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "commentId=" + commentId +
                ", text='" + text + '\'' +
                ", userId=" + userId +
                ", postId=" + postId +
                '}';
    }
}
