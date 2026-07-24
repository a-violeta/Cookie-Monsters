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
            consolePrinter.printError("Missing Arguments");
            consolePrinter.printExplanation("12 'Post ID' ");
            return;
        }
        else if (args.length > 1) {
            consolePrinter.printError("Too Many Arguments");
            consolePrinter.printExplanation("12 'postId'");
            return;
        }

        long postId = Long.parseLong(args[0]);
        postUseCases.deletePost(postId);
        consolePrinter.printSuccess("Post successfully deleted!");
    }
}