package com.app.console;

import com.app.model.Comment;
import com.app.model.Community;
import com.app.model.Post;
import com.app.model.User;

import java.time.format.DateTimeFormatter;

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
    private static final String BROWN = "\u001B[38;5;94m";

    public void printSuccess(String message) {
        System.out.println(GREEN + "✅ Success: " + message + RESET);
    }

    public void printError(String message) {
        System.out.println(RED + "❌ Error: " + message + RESET);
    }

    public void printExplanation(String usage) {
        System.out.println(CYAN + "ℹ️  Usage: " + usage + RESET);
    }

    public void printBanner() {
        System.out.println();
        System.out.println(BOLD + YELLOW + "                 .-\"\"\"-." + RESET);
        System.out.println(BOLD + YELLOW + "               / . " + BROWN + ".o." + YELLOW + "  \\" + RESET);
        System.out.println(BOLD + YELLOW + "              | . " + BROWN + "o." + YELLOW + "  " + BROWN + "o" + YELLOW + "  |" + RESET);
        System.out.println(BOLD + YELLOW + "              | " + BROWN + ".o o o " + YELLOW + ". |" + RESET);
        System.out.println(BOLD + YELLOW + "               \\  " + BROWN + ".o. " + YELLOW + ". /" + RESET);
        System.out.println(BOLD + YELLOW + "                '-...-'" + RESET);
        System.out.println();
        System.out.println(BOLD + CYAN + "   ██████╗ ██████╗  ██████╗ ██╗  ██╗██╗███████╗" + RESET);
        System.out.println(BOLD + CYAN + "  ██╔════╝██╔═══██╗██╔═══██╗██║ ██╔╝██║██╔════╝" + RESET);
        System.out.println(BOLD + CYAN + "  ██║     ██║   ██║██║   ██║█████╔╝ ██║█████╗  " + RESET);
        System.out.println(BOLD + CYAN + "  ██║     ██║   ██║██║   ██║██╔═██╗ ██║██╔══╝  " + RESET);
        System.out.println(BOLD + CYAN + "  ╚██████╗╚██████╔╝╚██████╔╝██║  ██╗██║███████╗" + RESET);
        System.out.println(BOLD + CYAN + "   ╚═════╝ ╚═════╝  ╚═════╝ ╚═╝  ╚═╝╚═╝╚══════╝" + RESET);
        System.out.println();
        System.out.println(BOLD + GREEN + "        🍪  M O N S T E R S   🍪" + RESET);
        System.out.println();
        System.out.println(GRAY + "        \"We want data... WE WANT DATA NOW!\"" + RESET);
        System.out.println();
    }

    public void printGoodbye() {
        System.out.println();
        System.out.println(YELLOW + "   🍪  " + RESET + BOLD + GREEN + "See you soon, cookie monster!" + RESET + YELLOW + "  🍪" + RESET);
        System.out.println(GRAY + "        \"We will be back... for more cookies.\"" + RESET);
        System.out.println();
    }

    public void printMainMenu() {
        System.out.println();
        System.out.println(CYAN + "┌────────────────────────────────────┐" + RESET);
        System.out.println(CYAN + "│ " + RESET + BOLD + "🍪 Main Menu" + RESET + "                       " + CYAN + "│" + RESET);
        System.out.println(CYAN + "├────────────────────────────────────┤" + RESET);
        System.out.println(CYAN + "│  " + RESET + "1. Login" + "                          " + CYAN + "│" + RESET);
        System.out.println(CYAN + "│  " + RESET + "2. Create Account" + "                 " + CYAN + "│" + RESET);
        System.out.println(CYAN + "│  " + RESET + "0. Exit" + "                           " + CYAN + "│" + RESET);
        System.out.println(CYAN + "└────────────────────────────────────┘" + RESET);
    }

    public void printPrompt(String label) {
        System.out.print(YELLOW + "➜ " + RESET + label + ": ");
    }

    public void printHomeMenu() {
        System.out.println();
        System.out.println(CYAN + "┌────────────────────────────────────┐" + RESET);
        System.out.println(CYAN + "│ " + RESET + BOLD + "🍪 Home Menu         " + RESET + "              " + CYAN + "│" + RESET);
        System.out.println(CYAN + "├────────────────────────────────────┤" + RESET);

        System.out.println(CYAN + "│  " + RESET + "0. Exit" + "                           " + CYAN + "│" + RESET);

        System.out.println(CYAN + "│  " + RESET + "1. List Communities" + "               " + CYAN + "│" + RESET);
        System.out.println(CYAN + "│  " + RESET + "2. Find Community" + "                 " + CYAN + "│" + RESET);
        System.out.println(CYAN + "│  " + RESET + "3. Create Community" + "               " + CYAN + "│" + RESET);
        System.out.println(CYAN + "│  " + RESET + "4. Join Community" + "                 " + CYAN + "│" + RESET);
        System.out.println(CYAN + "│  " + RESET + "5. Leave Community" + "                " + CYAN + "│" + RESET);
        System.out.println(CYAN + "│  " + RESET + "6. Edit Community" + "                 " + CYAN + "│" + RESET);
        System.out.println(CYAN + "│  " + RESET + "7. Delete Community" + "               " + CYAN + "│" + RESET);

        System.out.println(CYAN + "│  " + RESET + "8. Feed" + "                           " + CYAN + "│" + RESET);
        System.out.println(CYAN + "│  " + RESET + "9. List Posts" + "                     " + CYAN + "│" + RESET);
        System.out.println(CYAN + "│  " + RESET + "10. Add Post" + "                      " + CYAN + "│" + RESET);
        System.out.println(CYAN + "│  " + RESET + "11. Edit Post" + "                     " + CYAN + "│" + RESET);
        System.out.println(CYAN + "│  " + RESET + "12. Delete Post" + "                   " + CYAN + "│" + RESET);

        System.out.println(CYAN + "│  " + RESET + "13. List Comments" + "                 " + CYAN + "│" + RESET);
        System.out.println(CYAN + "│  " + RESET + "14. Add Comment" + "                   " + CYAN + "│" + RESET);
        System.out.println(CYAN + "│  " + RESET + "15. Edit Comment" + "                  " + CYAN + "│" + RESET);
        System.out.println(CYAN + "│  " + RESET + "16. Delete Comment" + "                " + CYAN + "│" + RESET);

        System.out.println(CYAN + "│  " + RESET + "17. Help" + "                          " + CYAN + "│" + RESET);
        System.out.println(CYAN + "│  " + RESET + "18. Logout" + "                        " + CYAN + "│" + RESET);

        System.out.println(CYAN + "└────────────────────────────────────┘" + RESET);
    }

    public void printHelp() {
        String[] lines = {
                "exit / 0               — Exit the application",
                "list-communities       — List all communities",
                "find-community         — Find a community by name",
                "create-community       — Create a new community",
                "join-community         — Join an existing community",
                "exit-community         — Leave a community",
                "edit-community         — Edit a community's name and description",
                "delete-community       — Delete a community",
                "posts-feed             — List all posts",
                "list-posts             — List all posts of a community",
                "add-post               — Create a post in a community",
                "edit-post              — Edit an existing post",
                "delete-post            — Delete a post",
                "list-comments          — List comments on a post",
                "add-comment            — Comment on a post",
                "edit-comment           — Edit an existing comment",
                "delete-comment         — Delete a comment",
                "help / h               — Display this help menu",
                "logout                 — Log out of your account"
        };

        int width = 4;
        for (String line : lines) {
            width = Math.max(width, line.length() + 4);
        }

        System.out.println();
        System.out.println(CYAN + "┌" + "─".repeat(width) + "┐" + RESET);
        printHelpLine(width, BOLD + "🍪 Reddit-CLI — Available Commands" + RESET);
        System.out.println(CYAN + "├" + "─".repeat(width) + "┤" + RESET);
        for (String line : lines) {
            printHelpLine(width, "  " + line);
        }
        System.out.println(CYAN + "└" + "─".repeat(width) + "┘" + RESET);
        System.out.println();
    }

    private void printHelpLine(int width, String content) {
        int visibleLength = content.replaceAll("\u001B\\[[;\\d]*m", "").length();
        int padding = Math.max(0, width - visibleLength - 1);
        System.out.println(ConsolePrinter.CYAN + "│ " + RESET + content + " ".repeat(padding) + ConsolePrinter.CYAN + "│" + RESET);
    }

    public void printPostLoginHint() {
        System.out.println();
        System.out.println(GRAY + "Commands are no longer numbered — " + RESET
                + "type the command name and its parameters to use it.");
        System.out.println(GRAY + "Type " + RESET + CYAN + "help" + RESET + GRAY
                + " or " + RESET + CYAN + "h" + RESET + GRAY + " to see all available commands." + RESET);
        System.out.println();
    }

    public void displayPost(Post post) {
        System.out.println("\n" + CYAN + "┌──────────────────────────────────────────────" + RESET);
        System.out.println(CYAN + "│ " + RESET + "📌 " + BOLD + post.getSubreddit().getDisplayName() + RESET
                + GRAY + "  •  Post #" + post.getId() + RESET);
        System.out.println(CYAN + "│ " + RESET + BOLD + YELLOW + post.getTitle() + RESET);
        System.out.println(CYAN + "│" + RESET);
        System.out.println(CYAN + "│ " + RESET + post.getContent());
        System.out.println(CYAN + "│" + RESET);
        System.out.println(CYAN + "│ " + RESET + "👤 " + GRAY + "author: " + post.getAuthor().getUsername() + RESET);
        System.out.println(CYAN + "└──────────────────────────────────────────────" + RESET + "\n");
    }

    public void displayCommunity(Community community) {
        System.out.println("\n" + PURPLE + "┌──────────────────────────────────────────────" + RESET);
        System.out.println(PURPLE + "│ " + RESET + "🌐 " + BOLD + community.getDisplayName() + RESET);
        System.out.println(PURPLE + "│" + RESET);
        System.out.println(PURPLE + "│ " + RESET + GRAY + community.getDescription() + RESET);
        System.out.println(PURPLE + "└──────────────────────────────────────────────" + RESET + "\n");
    }

    public void displayUser(User user) {
        System.out.println("\n" + BLUE + "┌──────────────────────────────────────────────" + RESET);
        System.out.println(BLUE + "│ " + RESET + "👤 " + BOLD + user.getUsername() + RESET);
        System.out.println(BLUE + "│" + RESET);
        System.out.println(BLUE + "│ " + RESET + user.getDescription());
        System.out.println(BLUE + "│" + RESET);
        System.out.println(BLUE + "│ " + RESET + GRAY + "joined: " + user.getCreatedAt() + RESET);
        System.out.println(BLUE + "└──────────────────────────────────────────────" + RESET + "\n");
    }

    public void displayComment(Comment comment) {
        System.out.println("\n" + GRAY + "┌──────────────────────────────────────────────" + RESET);
        System.out.println(GRAY + "│ " + RESET + "💬 " + comment.getText());
        System.out.println(GRAY + "│" + RESET);
        System.out.println(GRAY + "│ " + RESET + "👤 " + GRAY + "author: " + comment.getUser().getUsername() + RESET);
        System.out.println(GRAY + "└──────────────────────────────────────────────" + RESET + "\n");
    }

    public void printCommunityListItem(int index, Community community) {
        System.out.println(CYAN + " " + index + ". " + RESET + "🌐 " + community.getDisplayName());
    }

    public void printPostListItem(int index, Post post) {
        System.out.println(CYAN + " " + index + ". " + RESET + "📌 " + post.getTitle());
    }

    public void printCommentListItem(int index, Comment comment) {
        int maxLength = 40;
        String text = comment.getText();
        // the preview is the first 40 characters of the post followed by '...'
        String preview = text.length() > maxLength
                ? text.substring(0, maxLength) + "..."
                : text;

        System.out.println(CYAN + " " + index + ". " + RESET + "💬 " + preview);
    }
}