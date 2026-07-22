package com.app.console;

import com.app.model.Comment;
import com.app.model.Community;
import com.app.model.Post;
import com.app.model.User;

public class ConsolePrinter {

    // ANSI color codes
    private static final String RESET = "\u001B[0m";
    private static final String BOLD = "\u001B[1m";
    private static final String CYAN = "\u001B[36m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE = "\u001B[34m";
    private static final String PURPLE = "\u001B[35m";
    private static final String RED = "\u001B[31m";
    private static final String GRAY = "\u001B[90m";

    public void printSuccess(String message) {
        System.out.println(GREEN + "✅ Success: " + message + RESET);
    }

    public void printError(String message) {
        System.out.println(RED + "❌ Error: " + message + RESET);
    }

    public void printGoodbye() {
        String RESET = "\u001B[0m";
        String BOLD = "\u001B[1m";
        String YELLOW = "\u001B[33m";
        String GREEN = "\u001B[32m";
        String GRAY = "\u001B[90m";

        System.out.println();
        System.out.println(YELLOW + "   🍪  " + RESET + BOLD + GREEN + "See you soon, cookie monster!" + RESET + YELLOW + "  🍪" + RESET);
        System.out.println(GRAY + "        \"Me will be back... for more cookies.\"" + RESET);
        System.out.println();
    }

    public void displayPost(Post post) {
        System.out.println("\n" + CYAN + "┌──────────────────────────────────────────────" + RESET);
        System.out.println(CYAN + "│ " + RESET + "📌 " + BOLD + "r/" + post.getCommunityId() + RESET
                + GRAY + "  •  Post #" + post.getPostId() + RESET);
        System.out.println(CYAN + "│ " + RESET + BOLD + YELLOW + post.getTitle() + RESET);
        System.out.println(CYAN + "│" + RESET);
        System.out.println(CYAN + "│ " + RESET + post.getText());
        System.out.println(CYAN + "│" + RESET);
        System.out.println(CYAN + "│ " + RESET + "👤 " + GRAY + "author: " + post.getUserId() + RESET);
        System.out.println(CYAN + "└──────────────────────────────────────────────" + RESET + "\n");
    }

    public void displayCommunity(Community community) {
        System.out.println("\n" + PURPLE + "┌──────────────────────────────────────────────" + RESET);
        System.out.println(PURPLE + "│ " + RESET + "🌐 " + BOLD + community.getCommunityName() + RESET);
        System.out.println(PURPLE + "│" + RESET);
        System.out.println(PURPLE + "│ " + RESET + GRAY + community.getDescription() + RESET);
        System.out.println(PURPLE + "└──────────────────────────────────────────────" + RESET + "\n");
    }

    public void displayUser(User user) {
        System.out.println("\n" + BLUE + "┌──────────────────────────────────────────────" + RESET);
        System.out.println(BLUE + "│ " + RESET + "👤 " + BOLD + "u/" + user.getUsername() + RESET);
        System.out.println(BLUE + "└──────────────────────────────────────────────" + RESET + "\n");
    }

    public void displayComment(Comment comment) {
        System.out.println("\n" + GRAY + "┌──────────────────────────────────────────────" + RESET);
        System.out.println(GRAY + "│ " + RESET + "💬 " + comment.getText());
        System.out.println(GRAY + "│" + RESET);
        System.out.println(GRAY + "│ " + RESET + "👤 " + GRAY + "author: " + comment.getUserId() + RESET);
        System.out.println(GRAY + "└──────────────────────────────────────────────" + RESET + "\n");
    }
}