package com.app.console;

import com.app.model.Post;
import com.app.service.PostUseCases;

import java.util.List;

public class PostsFeedCommand extends Command {

    private final PostUseCases postUseCases;

    public PostsFeedCommand(ConsolePrinter printer, PostUseCases postUseCases) {
        super(printer);
        this.postUseCases = postUseCases;
    }

    @Override
    public void execute(String[] args) {
        if (args.length > 0) {

            consolePrinter.printError("Too Many Arguments");
            consolePrinter.printExplanation("list-posts");
            return;
        }

        List<Post> posts = postUseCases.listPosts();

        if (posts.isEmpty()) {
            consolePrinter.printError("No posts found!");
        } else {
            for (Post post : posts) {
                consolePrinter.displayPost(post);
            }
        }

    }
}