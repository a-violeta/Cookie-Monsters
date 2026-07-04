package main.model;

import java.time.LocalDate;

public class Comment {
    private static int nextId = 1;

    private int commentId;
    private String text;
    private String creationDate;
    private int userId;
    private int postId;

    public Comment(String text, int userId) {
        this.commentId = nextId++;
        this.creationDate = LocalDate.now().toString();
        this.userId = userId;

        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("Comment text is required");
        }

        this.text = text;
    }

    public int getCommentId() {
        return commentId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public int getUserId() {
        return userId;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    @Override
    public String toString() {
        return
                "\nComment id: " + commentId + '\n' +
                "Text: " + text + '\n' +
                "CreationDate: " + creationDate + '\n' +
                "User id: " + userId + '\n' +
                "Post id: " + postId + '\n';
    }
}
