package com.app.console;

import com.app.service.CommentUseCases;

public class EditCommentCommand extends Command{

    private final CommentUseCases commentUseCases;

    public EditCommentCommand(ConsolePrinter consolePrinter, CommentUseCases commentUseCases) {
        super(consolePrinter);
        this.commentUseCases = commentUseCases;
    }

    @Override
    public void execute(String[] args) {

        if (args.length < 2) {
            consolePrinter.printError("Missing Arguments");
            consolePrinter.printExplanation("edit-comment 'commentId' 'Text'");
            return;
        } else if (args.length > 2) {
            consolePrinter.printError("Too Many Arguments");
            consolePrinter.printExplanation("edit-comment 'commentId' 'Text'");
            return;
        }

        try {
            String newText = args[1];
            long commentId = Long.parseLong(args[0]); // Easier to read and understand the code

            commentUseCases.editComment(commentId, newText);
            consolePrinter.printSuccess("Comment successfully edited!");

        } catch (NumberFormatException e) {
            consolePrinter.printError("commentId must be a number.");
        } catch (Exception e) {
            consolePrinter.printError(e.getMessage());
        }
    }
}
