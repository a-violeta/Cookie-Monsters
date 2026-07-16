package Model;

import java.time.LocalDateTime;

public class Post {

    private static int idCounter = 1; // Needs to redo it

    private final int id;
    private final String title;
    private final String body;
    private final String community;
    private int upvotes;
    private final LocalDateTime createdAt;

    public Post(String title, String body, String community) {
        this.id = idCounter++;
        this.title = title;
        this.body = body;
        this.community = community;
        this.upvotes = 0;
        this.createdAt = LocalDateTime.now();
    }

    // Getters for the ConsolePrint Class
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getBody() { return body; }
    public String getCommunity() { return community; }
    public int getUpvotes() { return upvotes; }
}