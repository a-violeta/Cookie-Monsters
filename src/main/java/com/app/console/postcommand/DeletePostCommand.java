package com.app.console.postcommand;

import com.app.console.core.Command;
import com.app.console.core.ConsolePrinter;
import com.app.console.core.ConsoleReader;
import com.app.model.Post;
import com.app.service.PostUseCases;
import com.app.service.UserUseCases;

import java.util.List;
import java.util.UUID;

public class DeletePostCommand extends Command {
    private final PostUseCases postUseCases;
    private final ConsoleReader consoleReader;
    private final UserUseCases userUseCases;

    public DeletePostCommand(ConsolePrinter consolePrinter, PostUseCases postUseCases, ConsoleReader consoleReader, UserUseCases userUseCases) {
        super(consolePrinter);
        this.postUseCases = postUseCases;
        this.consoleReader = consoleReader;
        this.userUseCases = userUseCases;
    }

    @Override
    public void execute(String[] args) {
        if (args.length > 0) {
            consolePrinter.printError("Too Many Arguments");
            consolePrinter.printExplanation("delete-post");
            return;
        }

        try {
            List<Post> posts = postUseCases.listPosts(userUseCases.getLoggedInUser().getUsername());

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

            postUseCases.deletePost(postId, userUseCases.getLoggedInUser().getUsername());
            consolePrinter.printSuccess("Post successfully deleted!");
        } catch (Exception e){
            consolePrinter.printError(e.getMessage());
        }
    }
}