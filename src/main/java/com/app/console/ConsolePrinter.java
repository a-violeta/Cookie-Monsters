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

    public void printExplanation(String usage) {
        String CYAN = "\u001B[36m";
        String RESET = "\u001B[0m";

        System.out.println(CYAN + "ℹ️  Usage: " + usage + RESET);
    }

    public void printBanner() {
        String RESET = "\u001B[0m";
        String BOLD = "\u001B[1m";
        String YELLOW = "\u001B[33m";
        String BROWN = "\u001B[38;5;94m";
        String CYAN = "\u001B[36m";
        String GREEN = "\u001B[32m";

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
        System.out.println("\u001B[90m" + "        \"We want data... WE WANT DATA NOW!\"" + RESET);
        System.out.println();
    }

    public void printGoodbye() {
        String RESET = "\u001B[0m";
        String BOLD = "\u001B[1m";
        String YELLOW = "\u001B[33m";
        String GREEN = "\u001B[32m";
        String GRAY = "\u001B[90m";

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

    public void printHelp() {

        String[] lines = {
                "create-community <name> <description>   — Create a new community",
                "list-communities                        — List all communities",
                "find-community <name>                   — Find a community by name",
                "join-community <communityId>            — Join an existing community",
                "exit-community <communityId>            — Leave a community",
                "edit-community <communityId> <newDesc>  — Edit a community's description",
                "delete-community <communityId>          — Delete a community",
                //"remove-post <communityId> <postId>      — Remove a post from a community",
                "add-post <communityId> <title> <text>   — Create a post in a community",
                "posts-feed                              — List all posts",
                "list-posts <communityId>                — List all posts of a community",
                "edit-post <postId> <newText>            — Edit an existing post",
                "delete-post <postId>                    — Delete a post",
                "add-comment <postId> <text>             — Comment on a post",
                //"edit-comment <commentId> <newText>      — Edit an existing comment",
                "delete-comment <commentId>              — Delete a comment",
                "logout                                  — Log out of your account",
                "help / h                                — Display this help menu",
                "exit / 0                                — Exit the application"
        };

        int width = 4;
        for (String line : lines) {
            width = Math.max(width, line.length() + 4);
        }
        // for a border that fits all the text

        System.out.println();
        System.out.println(CYAN + "┌" + "─".repeat(width) + "┐" + RESET);
        printHelpLine(width, BOLD + "🍪 Reddit-CLI — Available Commands" + RESET, CYAN);
        System.out.println(CYAN + "├" + "─".repeat(width) + "┤" + RESET);
        for (String line : lines) {
            printHelpLine(width, "  " + line, CYAN);
        }
        System.out.println(CYAN + "└" + "─".repeat(width) + "┘" + RESET);
        System.out.println();
    }

    private void printHelpLine(int width, String content, String borderColor) {
        int visibleLength = content.replaceAll("\u001B\\[[;\\d]*m", "").length();
        int padding = Math.max(0, width - visibleLength - 1);
        System.out.println(borderColor + "│ " + RESET + content + " ".repeat(padding) + borderColor + "│" + RESET);
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
        System.out.println(CYAN + "│ " + RESET + "📌 " + BOLD + post.getCommunity().getCommunityName() + RESET
                + GRAY + "  •  Post #" + post.getId() + RESET);
        System.out.println(CYAN + "│ " + RESET + BOLD + YELLOW + post.getTitle() + RESET);
        System.out.println(CYAN + "│" + RESET);
        System.out.println(CYAN + "│ " + RESET + post.getText());
        System.out.println(CYAN + "│" + RESET);
        System.out.println(CYAN + "│ " + RESET + "👤 " + GRAY + "author: " + post.getUser().getUsername() + RESET);
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
        System.out.println(BLUE + "│ " + RESET + "👤 " + BOLD + user.getUsername() + RESET);
        System.out.println(BLUE + "└──────────────────────────────────────────────" + RESET + "\n");
    }

    public void displayComment(Comment comment) {
        System.out.println("\n" + GRAY + "┌──────────────────────────────────────────────" + RESET);
        System.out.println(GRAY + "│ " + RESET + "💬 " + comment.getText());
        System.out.println(GRAY + "│" + RESET);
        System.out.println(GRAY + "│ " + RESET + "👤 " + GRAY + "author: " + comment.getUser().getUsername() + RESET);
        System.out.println(GRAY + "└──────────────────────────────────────────────" + RESET + "\n");
    }
}