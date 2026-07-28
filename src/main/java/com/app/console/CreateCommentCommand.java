package com.app.console;

import com.app.service.CommentUseCases;
import com.app.service.UserUseCases;

public class CreateCommentCommand extends Command {

    private final CommentUseCases commentUseCases;
    private final UserUseCases userUseCases;

    public CreateCommentCommand(ConsolePrinter consolePrinter,CommentUseCases commentUseCases, UserUseCases userUseCases){
        super(consolePrinter);
        this.commentUseCases = commentUseCases;
        this.userUseCases = userUseCases;
    }

    @Override
    public void execute(String[] args) {

        if (args.length < 2) {
            consolePrinter.printError("Missing Arguments");
            consolePrinter.printExplanation("add-comment <Text> <userId> <postId>");
            return;
        } else if (args.length > 2) {
            consolePrinter.printError("Too Many Arguments");
            consolePrinter.printExplanation("add-comment <Text> <userId> <postId>");
            return;
        }

        try {
            String text = args[1];
            long userId = userUseCases.getLoggedInUser().getId();
            long postId = Long.parseLong(args[0]);

            commentUseCases.addComment(text, userId, postId);
            consolePrinter.printSuccess("Comment successfully created!");

        } catch (NumberFormatException e) {
            consolePrinter.printError("postId must be a number.");
        } catch (Exception e) {
            consolePrinter.printError(e.getMessage());
        }


    }
}
