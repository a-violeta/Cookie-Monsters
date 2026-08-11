package com.app.console;

import com.app.model.Comment;
import com.app.model.Community;
import com.app.model.Post;
import com.app.service.CommentUseCases;
import com.app.service.CommunityUseCases;
import com.app.service.PostUseCases;
import com.app.service.UserUseCases;

import java.util.List;
import java.util.UUID;

public class EditCommentCommand extends Command{

    private final CommentUseCases commentUseCases;
    private final ConsoleReader consoleReader;
    private final PostUseCases postUseCases;
    private final UserUseCases userUseCases;
    private final CommunityUseCases communityUseCases;

    public EditCommentCommand(ConsolePrinter consolePrinter, CommentUseCases commentUseCases, ConsoleReader consoleReader, PostUseCases postUseCases, UserUseCases userUseCases, CommunityUseCases communityUseCases) {
        super(consolePrinter);
        this.commentUseCases = commentUseCases;
        this.consoleReader = consoleReader;
        this.postUseCases = postUseCases;
        this.userUseCases = userUseCases;
        this.communityUseCases = communityUseCases;
    }

    @Override
    public void execute(String[] args) {

        if (args.length >= 1) {
            consolePrinter.printError("Too Many Arguments");
            return;
        }

        try {
            Long loggedInUserId = userUseCases.getLoggedInUser().getId();
            List<Community> communities = communityUseCases.listCommunitiesByUserId(loggedInUserId);
            // we needed a new method to do this

            for (int i = 0; i < communities.size(); i++) {
                consolePrinter.printCommunityListItem(i+1, communities.get(i));
            }

            consolePrinter.printPrompt("Choose a community by typing its index");

            // read with console
            // and check the number chosen for validity
            String communityInput = consoleReader.readLine();

            int communityChosenIndex;
            try {
                communityChosenIndex = Integer.parseInt(communityInput);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("'" + communityInput + "' is not a valid number!");
            }

            if (communityChosenIndex < 1 || communityChosenIndex > communities.size()) {
                throw new IllegalArgumentException("Index out of bounds!");
            }

            Community community = communities.get(communityChosenIndex-1);

            //List<Post> posts = postUseCases.listPosts(community.getId());
            List<Post> posts = communityUseCases.listCommunityPosts(community.getName());
            // once we have the community, we take all its posts

            for (int i = 0; i < posts.size(); i++) {
                consolePrinter.printPostListItem(i+1, posts.get(i));
            }

            consolePrinter.printPrompt("Choose a post by typing its index");

            // read with console
            // and check the number chosen for validity
            String postInput = consoleReader.readLine();

            int postChosenIndex;
            try {
                postChosenIndex = Integer.parseInt(postInput);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("'" + postInput + "' is not a valid number!");
            }

            if (postChosenIndex < 1 || postChosenIndex > posts.size()) {
                throw new IllegalArgumentException("Index out of bounds!");
            }

            Post post = posts.get(postChosenIndex-1);
            UUID postId = post.getId();

            List<Comment> comments = commentUseCases.listCommentByPostId(postId, userUseCases.getLoggedInUser().getUsername());

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

            UUID commentId = comments.get(chosenIndex-1).getId();

            consolePrinter.printPrompt("Type comment");

            // read with console
            String newText = consoleReader.readLine();

            commentUseCases.editComment(commentId, newText, userUseCases.getLoggedInUser().getUsername());
            consolePrinter.printSuccess("Comment successfully edited!");
        } catch (Exception e) {
            consolePrinter.printError(e.getMessage());
        }
    }
}