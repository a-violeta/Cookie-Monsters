package com.app.console;

import com.app.model.Comment;
import com.app.model.Community;
import com.app.model.Post;
import com.app.model.User;
import com.app.service.CommentUseCases;
import com.app.service.CommunityUseCases;
import com.app.service.UserUseCases;

import java.util.List;
import java.util.UUID;

public class CreateCommentCommand extends Command {

    private final CommentUseCases commentUseCases;
    private final CommunityUseCases communityUseCases;
    private final ConsoleReader consoleReader;
    private final UserUseCases userUseCases;

    public CreateCommentCommand(ConsolePrinter consolePrinter, CommentUseCases commentUseCases, CommunityUseCases communityUseCases, ConsoleReader consoleReader, UserUseCases userUseCases){
        super(consolePrinter);
        this.commentUseCases = commentUseCases;
        this.communityUseCases = communityUseCases;
        this.consoleReader = consoleReader;
        this.userUseCases = userUseCases;
    }

    @Override
    public void execute(String[] args) {

        if (args.length >= 1) {
            consolePrinter.printError("Too Many Arguments");
            return;
        }

        try {
            User requester = userUseCases.getLoggedInUser();

            String username = requester.getUsername();

            List<Community> communities = communityUseCases.listCommunitiesByUserId(requester.getId());
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
            UUID parentId = null;

            consolePrinter.printPrompt("Type comment");

            // read with console
            String text = consoleReader.readLine();

            Comment newComment = commentUseCases.addComment(text, postId, parentId, username);
            consolePrinter.printSuccess("Comment successfully created!");
            consolePrinter.displayComment(newComment);
        } catch (Exception e) {
            consolePrinter.printError(e.getMessage());
        }
    }
}
