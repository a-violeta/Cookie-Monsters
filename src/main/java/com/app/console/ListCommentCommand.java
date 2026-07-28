package com.app.console;

import com.app.model.Comment;
import com.app.service.CommentUseCases;

import java.util.List;

public class ListCommentCommand extends Command{

    private final CommentUseCases commentUseCases;

    ListCommentCommand(ConsolePrinter consolePrinter,CommentUseCases commentUseCases) {
        super(consolePrinter);
        this.commentUseCases = commentUseCases;
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 1) {
            consolePrinter.printError("Missing Arguments");
            consolePrinter.printExplanation("list-comments 'postId' ");
            return;
        }
        else if (args.length > 1) {
            consolePrinter.printError("Too Many Arguments");
            consolePrinter.printExplanation("list-comments 'postId'");
            return;
        }

        long postId = Long.parseLong(args[0]);
        List<Comment> comments = commentUseCases.listCommentByPostId(postId);

        if (comments.isEmpty()) {
            consolePrinter.printError("No Comments to list!");
            return;
        }
        for (Comment comment : comments) {
            consolePrinter.displayComment(comment);
        }


    }
}
