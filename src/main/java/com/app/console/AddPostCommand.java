package com.app.console;

import com.app.model.Post;
import com.app.service.PostUseCases;
import com.app.service.UserUseCases;

public class AddPostCommand extends Command {
    private final PostUseCases postUseCases;
    private final UserUseCases userUseCases;

    public AddPostCommand(ConsolePrinter consolePrinter,PostUseCases postUseCases, UserUseCases userUseCases) {
        super(consolePrinter);
        this.postUseCases = postUseCases;
        this.userUseCases = userUseCases;
    }

    @Override
    public void execute(String[] args) {
        // Arguments Validations
        if (args.length < 3) {
            consolePrinter.printError("Missing Arguments");
            consolePrinter.printExplanation("add-post 'Community ID' 'Title' 'Text' ");
            return;
        } else if (args.length > 3) {

            consolePrinter.printError("Too Many Arguments");
            consolePrinter.printExplanation("add-post 'Community ID' 'Title' 'Text' ");
            return;
        }

        long communityId = Long.parseLong(args[0]);
        //long userId = Long.parseLong(args[1]);
        long userId = userUseCases.getLoggedInUser().getId();
        String title = args[1];
        String text = args[2];

        Post newPost = postUseCases.addPost(communityId, userId, title, text);
        consolePrinter.printSuccess("Post successfully added!");
        consolePrinter.displayPost(newPost);
    }
}
