package com.app.console;

import com.app.service.CommentUseCases;

public class CreateCommentCommand extends Command {

    private CommentUseCases commentUseCases;

    public CreateCommentCommand(CommentUseCases commentUseCases){
        super();
        this.commentUseCases = commentUseCases;
    }

    @Override
    public void execute(String[] args) {

        if (args.length < 3) {
            System.out.println("Error : Missing Arguments");
            System.out.println("Usage : 5 'Text' <userId> <postId>");
            return;
        } else if (args.length > 3) {
            System.out.println("Error : Too Many Arguments");
            System.out.println("Usage : 5 'Text' <userId> <postId>");
            return;
        }

        try {
            String text = args[0];
            long userId = Long.parseLong(args[1]); // Easier to read and understand the code
            long postId = Long.parseLong(args[2]);

            commentUseCases.addComment(text, userId, postId);
            System.out.println("Comment successfully created!");

        } catch (NumberFormatException e) {
            System.out.println("Error : userId and postId must be numbers.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error : " + e.getMessage());
        }


    }
}
