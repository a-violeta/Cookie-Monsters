package com.app.console;

import com.app.service.PostUseCases;

public class DeletePostCommand extends Command {
    private final PostUseCases postUseCases;

    public DeletePostCommand(ConsolePrinter consolePrinter,PostUseCases postUseCases) {
        super(consolePrinter);
        this.postUseCases = postUseCases;
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 1) {
            consolePrinter.printError("Error : Missing Arguments");
            consolePrinter.printExplanation("Usage : 12 'Post ID' ");
            return;
        }

        long postId = Long.parseLong(args[0]);
        postUseCases.deletePost(postId);
        consolePrinter.printSuccess("Post successfully deleted!");
    }
}