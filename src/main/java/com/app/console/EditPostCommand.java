package com.app.console;

import com.app.service.PostUseCases;

public class EditPostCommand extends Command {
    private final PostUseCases postUseCases;

    public EditPostCommand(ConsolePrinter consolePrinter,PostUseCases postUseCases) {
        super(consolePrinter);
        this.postUseCases = postUseCases;
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            consolePrinter.printError("Error : Missing Arguments");
            consolePrinter.printExplanation("Usage : 21 'Post Id' 'New Text' ");
            return;
        } else if (args.length > 2) {
            consolePrinter.printError("Error : Too Many Arguments");
            consolePrinter.printExplanation("Usage : 21 'Post Id' 'New Text' ");
            return;
        }

        long postId = Long.parseLong(args[0]);
        String newText = args[1];

        postUseCases.editPost(postId, newText);
        consolePrinter.printSuccess("Post successfully edited!");
    }
}
