package com.app.console.postcommand;

import com.app.console.core.Command;
import com.app.console.core.ConsolePrinter;
import com.app.model.Post;
import com.app.service.PostAbstract;
import com.app.service.UserAbstract;

import java.util.List;

public class PostsFeedCommand extends Command {

    private final PostAbstract postAbstract;
    private final UserAbstract userAbstract;

    public PostsFeedCommand(ConsolePrinter printer, PostAbstract postAbstract, UserAbstract userAbstract) {
        super(printer);
        this.postAbstract = postAbstract;
        this.userAbstract = userAbstract;
    }

    @Override
    public void execute(String[] args) {
        if (args.length > 0) {

            consolePrinter.printError("Too Many Arguments");
            consolePrinter.printExplanation("list-posts");
            return;
        }

        List<Post> posts = postAbstract.listPosts(userAbstract.getLoggedInUser().getUsername());

        if (posts.isEmpty()) {
            consolePrinter.printError("No posts found!");
        } else {
            for (Post post : posts) {
                consolePrinter.displayPost(post);
            }
        }

    }
}