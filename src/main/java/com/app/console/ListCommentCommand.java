package com.app.console;

import com.app.model.Comment;
import com.app.model.Post;
import com.app.service.CommentUseCases;
import com.app.service.PostUseCases;

import java.util.List;

public class ListCommentCommand extends Command{

    private final CommentUseCases commentUseCases;
    private final PostUseCases postUseCases;
    private final ConsoleReader consoleReader;

    ListCommentCommand(ConsolePrinter consolePrinter,CommentUseCases commentUseCases, PostUseCases postUseCases, ConsoleReader consoleReader) {
        super(consolePrinter);
        this.commentUseCases = commentUseCases;
        this.postUseCases = postUseCases;
        this.consoleReader = consoleReader;
    }

    @Override
    public void execute(String[] args) {
        if (args.length > 0) {
            consolePrinter.printError("Too Many Arguments");
            consolePrinter.printExplanation("list-comments");
            return;
        }

        try {
            List<Post> posts = postUseCases.listPosts();

            for (int i = 0; i < posts.size(); i++) {
                consolePrinter.printPostListItem(i+1, posts.get(i));
            }

            consolePrinter.printPrompt("Choose a post by typing its index");

            // read with console
            // and check the number chosen for validity
            String input = consoleReader.readLine();

            int chosenIndex;
            try {
                chosenIndex = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("'" + input + "' is not a valid number!");
            }

            if (chosenIndex < 1 || chosenIndex > posts.size()) {
                throw new IllegalArgumentException("Index out of bounds!");
            }

            Post post = posts.get(chosenIndex-1);
            Long postId = post.getId();
            List<Comment> comments = commentUseCases.listCommentByPostId(postId);

            consolePrinter.displayPost(post);
            if (comments.isEmpty()) {
                consolePrinter.printError("No comments to list!");
                return;
            }
            for (Comment comment : comments) {
                consolePrinter.displayComment(comment);
            }
        } catch (Exception e){
            consolePrinter.printError(e.getMessage());
        }

    }
}
