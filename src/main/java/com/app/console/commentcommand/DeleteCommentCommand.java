package com.app.console.commentcommand;

import com.app.console.core.Command;
import com.app.console.core.ConsolePrinter;
import com.app.console.core.ConsoleReader;
import com.app.model.Comment;
import com.app.model.Community;
import com.app.model.Post;
import com.app.service.CommentAbstract;
import com.app.service.CommunityAbstract;
import com.app.service.UserAbstract;

import java.util.List;
import java.util.UUID;

public class DeleteCommentCommand extends Command {

    private final CommentAbstract commentAbstract;
    private final ConsoleReader consoleReader;
    private final CommunityAbstract communityAbstract;
    private final UserAbstract userAbstract;

    public DeleteCommentCommand(ConsolePrinter consolePrinter, CommentAbstract commentAbstract, ConsoleReader consoleReader, CommunityAbstract communityAbstract, UserAbstract userAbstract) {
        super(consolePrinter);
        this.commentAbstract = commentAbstract;
        this.consoleReader = consoleReader;
        this.communityAbstract = communityAbstract;
        this.userAbstract = userAbstract;
    }

    @Override
    public void execute(String[] args) {

        if (args.length > 0) {
            consolePrinter.printError("Too Many Arguments");
            consolePrinter.printExplanation("delete-comment");
            return;
        }

        try {
            Long loggedInUserId = userAbstract.getLoggedInUser().getId();
            List<Community> communities = communityAbstract.listCommunitiesByUserId(loggedInUserId);
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
            List<Post> posts = communityAbstract.listCommunityPosts(community.getName());
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

            List<Comment> comments = commentAbstract.listCommentByPostId(postId, userAbstract.getLoggedInUser().getUsername());

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

            commentAbstract.removeComment(commentId, userAbstract.getLoggedInUser().getUsername());
            consolePrinter.printSuccess("Comment successfully deleted!");
        } catch (Exception e) {
            consolePrinter.printError(e.getMessage());
        }
    }
}