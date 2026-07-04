package main.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Post {
    private static int nextId = 1;

    private int postId;
    private String title;
    private String text;
    private String creationDate;
    private int userId;
    private int communityId;
    private List<Comment> comments;

    public Post(String title, String text, int userId) {
        this.postId = nextId++;

        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title is required");
        }

        this.title = title;

        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("Text is required");
        }

        this.text = text;

        this.creationDate = LocalDate.now().toString();
        this.userId = userId;
        this.comments = new ArrayList<>();
    }

    public int getPostId() {
        return postId;
    }

    public String getTitle() {
        return title;
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

    public int getCommunityId() {
        return communityId;
    }

    public void setCommunityId(int communityId) {
        this.communityId = communityId;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public Comment addComment(String text, int userId) {
        Comment newComment = new Comment(text, userId);
        newComment.setPostId(postId);
        comments.add(newComment);

        return newComment;
    }

    public void removeComment(int commentId) {
        // removeIf returns false if no elements were removed
        if (!comments.removeIf(comment -> comment.getCommentId() == commentId)) {
            throw new IllegalArgumentException("Comment with id " + commentId + " not found");
        }
    }

    @Override
    public String toString() {
        return
                "\nPost id: " + postId + '\n' +
                "Title: " + title + '\n' +
                "Text: " + text + '\n' +
                "Creation date: " + creationDate + '\n' +
                "User id: " + userId + '\n' +
                "Community id: " + communityId + '\n' +
                "\nComments: \n" + comments;
    }
}
