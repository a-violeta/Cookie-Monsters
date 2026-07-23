package com.app.console;

import com.app.model.Post;
import com.app.service.PostUseCases;

public class AddPostCommand extends Command {
    private final PostUseCases postUseCases;

    public AddPostCommand(ConsolePrinter consolePrinter,PostUseCases postUseCases) {
        super(consolePrinter);
        this.postUseCases = postUseCases;
    }

    @Override
    public void execute(String[] args) {
        // Arguments Validations
        if (args.length < 4) {
            consolePrinter.printError("Error : Missing Arguments");
            consolePrinter.printExplanation("Usage : 4 'Community ID' 'User ID' 'Title' 'Text' ");
            return;
        } else if (args.length > 4) {

            consolePrinter.printError("Error : Too Many Arguments");
            consolePrinter.printExplanation("Usage : 4 'Community ID' 'User ID' 'Title' 'Text' ");
            return;
        }

        long communityId = Long.parseLong(args[0]);
        long userId = Long.parseLong(args[1]);
        String title = args[2];
        String text = args[3];

        Post newPost = postUseCases.addPost(communityId, userId, title, text);
        consolePrinter.printSuccess("Post successfully added!");
        consolePrinter.displayPost(newPost);
    }
}
