package com.app.console;

import com.app.model.Comment;
import com.app.model.Post;
import com.app.service.CommentUseCases;

import java.util.List;

public class EditCommentCommand extends Command{

    private final CommentUseCases commentUseCases;
    private final ConsoleReader consoleReader;

    public EditCommentCommand(ConsolePrinter consolePrinter, CommentUseCases commentUseCases, ConsoleReader consoleReader) {
        super(consolePrinter);
        this.commentUseCases = commentUseCases;
        this.consoleReader = consoleReader;
    }

    @Override
    public void execute(String[] args) {

        if (args.length < 1) {
            consolePrinter.printError("Missing Arguments");
            consolePrinter.printExplanation("edit-comment  'Text'");
            return;
        } else if (args.length > 1) {
            consolePrinter.printError("Too Many Arguments");
            consolePrinter.printExplanation("edit-comment 'Text'");
            return;
        }

        try {
            List<Comment> comments = commentUseCases.listComments();

            for (int i = 0; i < comments.size(); i++) {
                consolePrinter.printCommentListItem(i+1, comments.get(i));
            }

            consolePrinter.printPrompt("Choose a comment by typing its index");

            // read with console
            // and check the number chosen for validity
            String input = consoleReader.readLine();

            int chosenIndex;
            try {
                chosenIndex = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("'" + input + "' is not a valid number!");
            }

            if (chosenIndex < 1 || chosenIndex > comments.size()) {
                throw new IllegalArgumentException("Index out of bounds!");
            }

            Long commentId = comments.get(chosenIndex-1).getId();

            String newText = args[0];

            commentUseCases.editComment(commentId, newText);
            consolePrinter.printSuccess("Comment successfully edited!");
        } catch (Exception e) {
            consolePrinter.printError(e.getMessage());
        }
    }
}
