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
        // Arguments Validations
        if (args.length < 2) {
            consolePrinter.printError("Missing Arguments");
            consolePrinter.printExplanation("add-post 'Title' 'Text' ");
            return;
        } else if (args.length > 2) {

            consolePrinter.printError("Too Many Arguments");
            consolePrinter.printExplanation("add-post 'Title' 'Text' ");
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

            Long communityId = communities.get(chosenIndex-1).getId();

            long userId = userUseCases.getLoggedInUser().getId();

            String title = args[0];
            String text = args[1];

            Post newPost = postUseCases.addPost(communityId, userId, title, text);
            consolePrinter.printSuccess("Post successfully added!");
            consolePrinter.displayPost(newPost);
        } catch (Exception e){
            consolePrinter.printError(e.getMessage());
        }
    }
}
