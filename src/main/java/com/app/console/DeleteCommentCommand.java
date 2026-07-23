package com.app.console;

import com.app.service.CommentUseCases;

public class DeleteCommentCommand extends Command {

    private final CommentUseCases commentUseCases;

    public DeleteCommentCommand(CommentUseCases commentUseCases) {
        super();
        this.commentUseCases = commentUseCases;
    }

    @Override
    public void execute(String[] args) {

        if (args.length < 1) {
            System.out.println("Error : Missing Arguments");
            System.out.println("Usage : 13 <commentId> ");
            return;
        } else if (args.length > 1) {
            System.out.println("Error : Too Many Arguments");
            System.out.println("Usage : 13 <commentId> ");
            return;
        }

        try {
            long commentId = Long.parseLong(args[0]);

            commentUseCases.removeComment(commentId);
            System.out.println("Comment successfully deleted!");

        } catch (NumberFormatException e) {
            System.out.println("Error : commentId must be a number.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error : " + e.getMessage());
        }
    }
}
