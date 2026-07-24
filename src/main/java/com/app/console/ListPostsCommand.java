package com.app.console;

import com.app.model.Post;
import com.app.service.PostUseCases;

import java.util.List;

public class ListPostsCommand extends Command {

    private final PostUseCases postUseCases;

    public ListPostsCommand(ConsolePrinter printer, PostUseCases postUseCases) {
        super(printer);
        this.postUseCases = postUseCases;
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 1) {
            consolePrinter.printError("Error : Missing Arguments");
            consolePrinter.printExplanation("Usage : 8 'Community ID' ");
            return;
        }

        long communityId = Long.parseLong(args[0]);
        List<Post> posts = postUseCases.listPosts(communityId);

        if (posts.isEmpty()) {
            consolePrinter.printError("No posts to list!");
            return;
        }
        for (Post post : posts) {
            consolePrinter.displayPost(post);
        }
    }
}
