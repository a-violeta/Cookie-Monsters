package com.app.console;

import com.app.model.Post;
import com.app.service.PostUseCases;

import java.util.List;
import java.util.UUID;

public class EditPostCommand extends Command {
    private final PostUseCases postUseCases;
    private final ConsoleReader consoleReader;

    public EditPostCommand(ConsolePrinter consolePrinter,PostUseCases postUseCases, ConsoleReader consoleReader) {
        super(consolePrinter);
        this.postUseCases = postUseCases;
        this.consoleReader = consoleReader;
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 1) {
            consolePrinter.printError("Missing Arguments");
            consolePrinter.printExplanation("edit-post 'New Text' ");
            return;
        } else if (args.length > 1) {
            consolePrinter.printError("Too Many Arguments");
            consolePrinter.printExplanation("edit-post 'New Text' ");
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

            UUID postId = posts.get(chosenIndex-1).getId();

            String newText = args[0];

            postUseCases.editPost(postId, null, newText);
            consolePrinter.printSuccess("Post successfully edited!");

            List<Post> postsAfterChange = postUseCases.listPosts();
            Post changedPost = postsAfterChange.get(chosenIndex-1);
            consolePrinter.displayPost(changedPost);
        } catch (Exception e){
            consolePrinter.printError(e.getMessage());
        }
    }
}
