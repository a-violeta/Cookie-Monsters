package com.app.console;

import com.app.model.Community;
import com.app.model.Post;
import com.app.service.CommunityUseCases;
import com.app.service.PostUseCases;

import java.util.List;
import java.util.UUID;

public class ListPostsCommand extends Command {

    private final PostUseCases postUseCases;
    private final CommunityUseCases communityUseCases;
    private final ConsoleReader consoleReader;

    public ListPostsCommand(ConsolePrinter printer, PostUseCases postUseCases, CommunityUseCases communityUseCases, ConsoleReader consoleReader) {
        super(printer);
        this.postUseCases = postUseCases;
        this.communityUseCases = communityUseCases;
        this.consoleReader = consoleReader;
    }

    @Override
    public void execute(String[] args) {
        if (args.length > 0) {
            consolePrinter.printError("Too Many Arguments");
            consolePrinter.printExplanation("list-posts");
            return;
        }

        try {

            List<Community> communities = communityUseCases.listCommunities();

            for (int i = 0; i < communities.size(); i++) {
                consolePrinter.printCommunityListItem(i+1, communities.get(i));
            }

            consolePrinter.printPrompt("Choose a community by typing its index");

            // read with console
            // and check the number chosen for validity
            String input = consoleReader.readLine();

            int chosenIndex;
            try {
                chosenIndex = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("'" + input + "' is not a valid number!");
            }

            if (chosenIndex < 1 || chosenIndex > communities.size()) {
                throw new IllegalArgumentException("Index out of bounds!");
            }

            UUID communityId = communities.get(chosenIndex-1).getId();

            List<Post> posts = postUseCases.listPosts(communityId);

            if (posts.isEmpty()) {
                consolePrinter.printError("No posts to list!");
                return;
            }
            for (Post post : posts) {
                consolePrinter.displayPost(post);
            }
        } catch (Exception e){
            consolePrinter.printError(e.getMessage());
        }
    }
}
