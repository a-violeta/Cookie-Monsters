package com.app.console;

import com.app.service.CommentUseCases;
import com.app.service.CommunityUseCases;
import com.app.service.PostUseCases;
import com.app.service.UserUseCases;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputParser {

    private final CommunityUseCases communityUseCases;
    private final PostUseCases postUseCases;
    private final UserUseCases userUseCases;
    private final CommentUseCases commentUseCases;

    private final ConsoleReader reader;
    private final ConsolePrinter printer;
    private final Map<String, Command> commandMap = new HashMap<>();

    public InputParser(ConsoleReader reader, ConsolePrinter printer, CommunityUseCases communityUseCases, CommentUseCases commentUseCases, PostUseCases postUseCases, UserUseCases userUseCases) {
        this.reader = reader;
        this.printer = printer;
        this.communityUseCases = communityUseCases;
        this.commentUseCases = commentUseCases;
        this.userUseCases = userUseCases;
        this.postUseCases = postUseCases;

        commandMap.put("create-community", new CreateCommunityCommand(printer, communityUseCases));
        commandMap.put("list-communities", new ListCommunityCommand(printer, communityUseCases));
        commandMap.put("logout", new LogoutCommand(printer, userUseCases));
        commandMap.put("exit", new ExitCommand(printer));
        commandMap.put("0", new ExitCommand(printer));
        commandMap.put("delete-community", new DeleteCommunityCommand(printer, communityUseCases));
        commandMap.put("edit-community", new EditCommunityCommand(printer, communityUseCases, reader));
        commandMap.put("exit-community", new ExitCommunityCommand(printer, communityUseCases, userUseCases, reader));
        commandMap.put("find-community", new FindCommunityCommand(printer, communityUseCases));
        commandMap.put("join-community", new JoinCommunityCommand(printer, communityUseCases, userUseCases, reader));
        commandMap.put("edit-comment", new EditCommentCommand(printer, commentUseCases, reader, postUseCases, userUseCases, communityUseCases));
        commandMap.put("delete-comment", new DeleteCommentCommand(printer, commentUseCases, reader, communityUseCases, userUseCases));
        commandMap.put("add-comment", new CreateCommentCommand(printer, commentUseCases, communityUseCases, reader, userUseCases));
        commandMap.put("help", new HelpCommand(printer));
        commandMap.put("h", new HelpCommand(printer));
        commandMap.put("add-post", new AddPostCommand(printer, postUseCases, userUseCases, communityUseCases, reader));
        commandMap.put("posts-feed", new PostsFeedCommand(printer, postUseCases));
        commandMap.put("list-posts", new ListPostsCommand(printer, postUseCases, communityUseCases, reader));
        commandMap.put("delete-post", new DeletePostCommand(printer, postUseCases, reader));
        commandMap.put("edit-post", new EditPostCommand(printer, postUseCases, reader));
        commandMap.put("list-comments", new ListCommentCommand(printer,commentUseCases, reader, communityUseCases, userUseCases, postUseCases));

        // Add Commands Classes to the map of commands
    }

    private String[] tokenizeInput(String input) {
        List<String> tokens = new ArrayList<>();

        // 1. Searching for Quotations marks
        // 2. Searching for every word without Spaces
        Pattern pattern = Pattern.compile("\"([^\"]*)\"|(\\S+)");
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            if (matcher.group(1) != null) {
                // Found Quotations Marks
                tokens.add(matcher.group(1));
            } else {
                // Found a plain word
                tokens.add(matcher.group(2));
            }
        }

        // Convert List to Array to work with the command interface
        return tokens.toArray(new String[0]);
    }

    public void startListening() {
        // while user is logged, parser will read commands
        // after logout call userUserCase.logout, the loggerInUser=null and exit while
        while(userUseCases.getLoggedInUser() != null) {
            try {
                reader.cliPrompt();
                String input = reader.readLine();
                String[] parts = tokenizeInput(input);

                if (parts.length == 0) continue;

                Command command = commandMap.get(parts[0].toLowerCase());

                if (command != null) {
                    command.execute(Arrays.copyOfRange(parts, 1, parts.length));
                } else {
                    printer.printError("Unknown Command");
                    Command helpCommand = commandMap.get("help");
                    helpCommand.execute(new String[0]);
                }
            } catch (Exception e) {
                printer.printError("Error : " + e.getMessage());
            }
        }
    }
}