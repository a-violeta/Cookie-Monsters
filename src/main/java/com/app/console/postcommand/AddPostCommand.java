package com.app.console.postcommand;

import com.app.console.core.Command;
import com.app.console.core.ConsolePrinter;
import com.app.console.core.ConsoleReader;
import com.app.model.Community;
import com.app.model.Post;
import com.app.service.CommunityAbstract;
import com.app.service.PostAbstract;
import com.app.service.UserAbstract;

import java.util.List;

public class AddPostCommand extends Command {
    private final PostAbstract postAbstract;
    private final UserAbstract userAbstract;
    private final CommunityAbstract communityAbstract;
    private final ConsoleReader consoleReader;

    public AddPostCommand(ConsolePrinter consolePrinter, PostAbstract postAbstract, UserAbstract userAbstract, CommunityAbstract communityAbstract, ConsoleReader consoleReader) {
        super(consolePrinter);
        this.postAbstract = postAbstract;
        this.userAbstract = userAbstract;
        this.communityAbstract = communityAbstract;
        this.consoleReader = consoleReader;
    }

    @Override
    public void execute(String[] args) {

        if (args.length >= 1) {

            consolePrinter.printError("Too Many Arguments");
            return;
        }

        try {

            List<Community> communities = communityAbstract.listCommunities();

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

            String username = userAbstract.getLoggedInUser().getUsername();

            consolePrinter.printPrompt("Type post title");

            // read with console
            String title = consoleReader.readLine();

            consolePrinter.printPrompt("Type post body");

            // read with console
            String text = consoleReader.readLine();

            Post newPost = postAbstract.addPost(title, text, subredditName, username, null, 1);
            consolePrinter.printSuccess("Post successfully added!");
            consolePrinter.displayPost(newPost);
        } catch (Exception e){
            consolePrinter.printError(e.getMessage());
        }
    }
}