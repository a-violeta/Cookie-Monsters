package com.app.console;

import com.app.service.CommentUseCases;

public class DeleteCommentCommand extends Command {

    private final CommentUseCases commentUseCases;

    public DeleteCommentCommand(ConsolePrinter consolePrinter,CommentUseCases commentUseCases) {
        super(consolePrinter);
        this.commentUseCases = commentUseCases;
    }

    @Override
    public void execute(String[] args) {

        if (args.length < 1) {
            consolePrinter.printError("Missing Arguments");
            consolePrinter.printExplanation("delete-comment 'commentId' ");
            return;
        } else if (args.length > 1) {
            consolePrinter.printError("Too Many Arguments");
            consolePrinter.printExplanation("delete-comment 'commentId' ");
            return;
        }

        try {
            long commentId = Long.parseLong(args[0]);

            commentUseCases.removeComment(commentId);
            consolePrinter.printSuccess("Comment successfully deleted!");

        } catch (NumberFormatException e) {
            consolePrinter.printError("commentId must be a number.");
        } catch (Exception e) {
            consolePrinter.printError(e.getMessage());
        }
    }
}
