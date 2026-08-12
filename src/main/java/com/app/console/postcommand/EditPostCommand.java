package com.app.console.postcommand;

import com.app.console.core.Command;
import com.app.console.core.ConsolePrinter;
import com.app.console.core.ConsoleReader;
import com.app.model.Post;
import com.app.service.PostAbstract;
import com.app.service.UserAbstract;

import java.util.List;
import java.util.UUID;

public class EditPostCommand extends Command {
    private final PostAbstract postAbstract;
    private final ConsoleReader consoleReader;
    private final UserAbstract userAbstract;

    public EditPostCommand(ConsolePrinter consolePrinter, PostAbstract postAbstract, ConsoleReader consoleReader, UserAbstract userAbstract) {
        super(consolePrinter);
        this.postAbstract = postAbstract;
        this.consoleReader = consoleReader;
        this.userAbstract = userAbstract;
    }

    @Override
    public void execute(String[] args) {
        if (args.length >= 1) {
            consolePrinter.printError("Too Many Arguments");
            return;
        }

        try {
            List<Post> posts = postAbstract.listPosts(userAbstract.getLoggedInUser().getUsername());

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

            consolePrinter.printPrompt("Type new post body");

            // read with console
            String newText = consoleReader.readLine();

            postAbstract.editPost(postId, null, newText, userAbstract.getLoggedInUser().getUsername() );
            consolePrinter.printSuccess("Post successfully edited!");

            List<Post> postsAfterChange = postAbstract.listPosts(userAbstract.getLoggedInUser().getUsername());
            Post changedPost = postsAfterChange.get(chosenIndex-1);
            consolePrinter.displayPost(changedPost);
        } catch (Exception e){
            consolePrinter.printError(e.getMessage());
        }
    }
}