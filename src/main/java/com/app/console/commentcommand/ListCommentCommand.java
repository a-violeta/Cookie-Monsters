package com.app.console.commentcommand;

import com.app.console.core.Command;
import com.app.console.core.ConsolePrinter;
import com.app.console.core.ConsoleReader;
import com.app.model.Comment;
import com.app.model.Community;
import com.app.model.Post;
import com.app.service.CommentUseCases;
import com.app.service.CommunityUseCases;
import com.app.service.PostUseCases;
import com.app.service.UserUseCases;

import java.util.List;
import java.util.UUID;

public class ListCommentCommand extends Command {

    private final CommentUseCases commentUseCases;
    private final ConsoleReader consoleReader;
    private final CommunityUseCases communityUseCases;
    private final UserUseCases userUseCases;
    private final PostUseCases postUseCases;

    ListCommentCommand(ConsolePrinter consolePrinter, CommentUseCases commentUseCases, ConsoleReader consoleReader, CommunityUseCases communityUseCases, UserUseCases userUseCases, PostUseCases postUseCases) {
        super(consolePrinter);
        this.commentUseCases = commentUseCases;
        this.consoleReader = consoleReader;
        this.communityUseCases = communityUseCases;
        this.userUseCases = userUseCases;
        this.postUseCases = postUseCases;
    }

    @Override
    public void execute(String[] args) {
        if (args.length > 0) {
            consolePrinter.printError("Too Many Arguments");
            consolePrinter.printExplanation("list-comments");
            return;
        }

        try {
            Long loggedInUserId = userUseCases.getLoggedInUser().getId();
            List<Community> communities = communityUseCases.listCommunitiesByUserId(loggedInUserId);

            if (communities.isEmpty()) {
                consolePrinter.printError("No communities found for your user!");
                return;
            }

            for (int i = 0; i < communities.size(); i++) {
                consolePrinter.printCommunityListItem(i + 1, communities.get(i));
            }

            consolePrinter.printPrompt("Choose a community by typing its index");
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

            Community community = communities.get(communityChosenIndex - 1);
            List<Post> posts = postUseCases.listPosts(community.getId());

            if (posts.isEmpty()) {
                consolePrinter.printError("No posts found in this community!");
                return;
            }

            for (int i = 0; i < posts.size(); i++) {
                consolePrinter.printPostListItem(i + 1, posts.get(i));
            }

            consolePrinter.printPrompt("Choose a post by typing its index");
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

            Post post = posts.get(postChosenIndex - 1);
            UUID postId = post.getId();
            List<Comment> comments = commentUseCases.listCommentByPostId(postId, userUseCases.getLoggedInUser().getUsername());

            consolePrinter.displayPost(post);
            if (comments.isEmpty()) {
                consolePrinter.printError("No comments to list!");
                return;
            }
            for (Comment comment : comments) {
                consolePrinter.displayComment(comment);
            }
        } catch (Exception e) {
            consolePrinter.printError(e.getMessage());
        }
    }
}