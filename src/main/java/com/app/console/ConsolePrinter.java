package com.app.console;

import com.app.model.Comment;
import com.app.model.Community;
import com.app.model.Post;
import com.app.model.User;

public class ConsolePrinter {

    public void printSuccess(String message) {
        System.out.println("Succes" + message);
    }

    public void printError(String message) {
        System.out.println("Error" + message);
    }

    public void displayPost(Post post) {
        System.out.println("\n┌──────────────────────────────────────────────");
        System.out.println("│ r/" + post.getCommunityId() + " • Post #" + post.getPostId());
        System.out.println("│ " + post.getTitle());
        System.out.println("│");
        System.out.println("│ " + post.getText());
        System.out.println("│");
        System.out.println("│ " + "author: " + post.getUserId());
        System.out.println("└──────────────────────────────────────────────\n");
    }

    public void displayCommunity(Community community) {
        System.out.println("\n┌──────────────────────────────────────────────");
        System.out.println("│ r/" + community.getCommunityName());
        System.out.println("│");
        System.out.println("│ " + community.getDescription());
        System.out.println("└──────────────────────────────────────────────\n");
    }

    public void displayUser(User user) {
        System.out.println("\n┌──────────────────────────────────────────────");
        System.out.println("│ u/" + user.getUsername());
        System.out.println("└──────────────────────────────────────────────\n");
    }

    public void displayComment(Comment comment) {
        System.out.println("\n┌──────────────────────────────────────────────");
        System.out.println("│ " + comment.getText());
        System.out.println("│");
        System.out.println("│ " + "author: " + comment.getUserId());
        System.out.println("└──────────────────────────────────────────────\n");
    }
}