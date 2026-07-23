package com.app.console;

import com.app.service.CommentUseCases;

public class EditCommentCommand extends Command{

    private final CommentUseCases commentUseCases;

    public EditCommentCommand(CommentUseCases commentUseCases) {
        super();
        this.commentUseCases = commentUseCases;
    }

    @Override
    public void execute(String[] args) {

        if (args.length < 2) {
            System.out.println("Error : Missing Arguments");
            System.out.println("Usage : 20 <commentId> 'Text'");
            return;
        } else if (args.length > 2) {
            System.out.println("Error : Too Many Arguments");
            System.out.println("Usage : 20 <commentId> 'Text'");
            return;
        }

        try {
            String newText = args[1];
            long commentId = Long.parseLong(args[0]); // Easier to read and understand the code

            commentUseCases.editComment(commentId, newText);
            System.out.println("Comment successfully edited!");

        } catch (NumberFormatException e) {
            System.out.println("Error : commentId must be a number.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error : " + e.getMessage());
        }
    }
}
