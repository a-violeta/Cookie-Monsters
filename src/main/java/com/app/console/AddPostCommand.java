package com.app.console;

import com.app.model.Community;
import com.app.model.Post;
import com.app.service.CommunityUseCases;
import com.app.service.PostUseCases;
import com.app.service.UserUseCases;

import java.util.List;

public class AddPostCommand extends Command {
    private final PostUseCases postUseCases;
    private final UserUseCases userUseCases;
    private final CommunityUseCases communityUseCases;
    private final ConsoleReader consoleReader;

    public AddPostCommand(ConsolePrinter consolePrinter,PostUseCases postUseCases, UserUseCases userUseCases, CommunityUseCases communityUseCases, ConsoleReader consoleReader) {
        super(consolePrinter);
        this.postUseCases = postUseCases;
        this.userUseCases = userUseCases;
        this.communityUseCases = communityUseCases;
        this.consoleReader = consoleReader;
    }

    @Override
    public void execute(String[] args) {

        if (args.length >= 1) {

            consolePrinter.printError("Too Many Arguments");
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

            String subredditName = communities.get(chosenIndex-1).getName();

            String username = userUseCases.getLoggedInUser().getUsername();

            consolePrinter.printPrompt("Type post title");

            // read with console
            String title = consoleReader.readLine();

            consolePrinter.printPrompt("Type post body");

            // read with console
            String text = consoleReader.readLine();

            Post newPost = postUseCases.addPost(title, text, subredditName, username);
            consolePrinter.printSuccess("Post successfully added!");
            consolePrinter.displayPost(newPost);
        } catch (Exception e){
            consolePrinter.printError(e.getMessage());
        }
    }
}