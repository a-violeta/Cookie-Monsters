package View;

import Model.Comment;
import Model.Community;
import Model.Post;
import Model.User;

public class ConsolePrinter {

    public void printSuccess(String message) {
        System.out.println("Succes" + message);
    }

    public void printError(String message) {
        System.out.println("Error" + message);
    }

    public void displayPost(Post post) {
        System.out.println("\n┌──────────────────────────────────────────────");
        System.out.println("│ r/" + post.getCommunity() + " • Post #" + post.getId());
        System.out.println("│ " + post.getTitle());
        System.out.println("│");
        System.out.println("│ " + post.getBody());
        System.out.println("│");
        System.out.println("│ " + post.getUpvotes() + " Upvotes");
        System.out.println("└──────────────────────────────────────────────\n");
    }

    public void displayCommunity(Community community) {

    }

    public void displayUser(User user) {

    }

    public void displayComment (Comment comment) {

    }
}

