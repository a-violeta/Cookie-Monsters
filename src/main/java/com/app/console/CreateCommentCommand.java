package com.app.console;

import com.app.model.Comment;
import com.app.model.Post;
import com.app.service.CommentUseCases;
import com.app.service.UserUseCases;
import com.app.service.PostUseCases;

import java.util.List;

public class CreateCommentCommand extends Command {

    private final CommentUseCases commentUseCases;
    private final PostUseCases postUseCases;
    private final ConsoleReader consoleReader;
    private final UserUseCases userUseCases;

    public CreateCommentCommand(ConsolePrinter consolePrinter, CommentUseCases commentUseCases, PostUseCases postUseCases, ConsoleReader consoleReader, UserUseCases userUseCases){
        super(consolePrinter);
        this.commentUseCases = commentUseCases;
        this.postUseCases = postUseCases;
        this.consoleReader = consoleReader;
        this.userUseCases = userUseCases;
    }

    @Override
    public void execute(String[] args) {

        if (args.length < 1) {
            consolePrinter.printError("Missing Arguments");
            consolePrinter.printExplanation("add-comment <Text>");
            return;
        } else if (args.length > 1) {
            consolePrinter.printError("Too Many Arguments");
            consolePrinter.printExplanation("add-comment <Text>");
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

            Long postId = posts.get(chosenIndex-1).getId();

            String text = args[0];
            long userId = userUseCases.getLoggedInUser().getId();

            Comment newComment = commentUseCases.addComment(text, userId, postId);
            consolePrinter.printSuccess("Comment successfully created!");
            consolePrinter.displayComment(newComment);
        } catch (Exception e) {
            consolePrinter.printError(e.getMessage());
        }
    }
}
