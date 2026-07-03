package consoleApp;

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

    Post(){
        this.postId= incrementId();
        this.communityId=0;
        this.userId=0;
        this.title="";
        this.text="";
        this.commentList=null;
    }

    Post(long communityId, long userId, String title, String text, List<Comment> commentList){
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

    @Override
    public String toString() {
        return super.toString();
    }

    void addComment(Comment comment){
        if(!comment.getText().isEmpty() && comment.getPostId()!=this.getPostId() && comment.getUserId()!=0)
            // validates text not empty, post is the same, user is not nonexistent

            commentList.add(comment);
        else
            System.out.println("Cannot add invalid comment! Check comment text, user and post");
    }

    void removeComment(long commentId){
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
