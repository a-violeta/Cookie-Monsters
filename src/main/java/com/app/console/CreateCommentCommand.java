package com.app.console;

import com.app.service.CommentUseCases;

public class CreateCommentCommand extends Command {

    private final CommentUseCases commentUseCases;

    public CreateCommentCommand(ConsolePrinter consolePrinter,CommentUseCases commentUseCases){
        super(consolePrinter);
        this.commentUseCases = commentUseCases;
    }

    @Override
    public void execute(String[] args) {

        if (args.length < 3) {
            consolePrinter.printError("Missing Arguments");
            consolePrinter.printExplanation("add-comment <Text> <userId> <postId>");
            return;
        } else if (args.length > 3) {
            consolePrinter.printError("Too Many Arguments");
            consolePrinter.printExplanation("add-comment <Text> <userId> <postId>");
            return;
        }

        try {
            String text = args[0];
            long userId = Long.parseLong(args[1]); // Easier to read and understand the code
            long postId = Long.parseLong(args[2]);

            commentUseCases.addComment(text, userId, postId);
            consolePrinter.printSuccess("Comment successfully created!");

        } catch (NumberFormatException e) {
            consolePrinter.printError("userId and postId must be numbers.");
        } catch (Exception e) {
            consolePrinter.printError(e.getMessage());
        }


    }
}
