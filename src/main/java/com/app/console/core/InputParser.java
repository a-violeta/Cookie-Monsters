package com.app.console.core;

import com.app.console.postcommand.PostsFeedCommand;
import com.app.console.clicommand.ExitCommand;
import com.app.console.clicommand.HelpCommand;
import com.app.console.commentcommand.CreateCommentCommand;
import com.app.console.commentcommand.DeleteCommentCommand;
import com.app.console.commentcommand.EditCommentCommand;
import com.app.console.commentcommand.ListCommentCommand;
import com.app.console.communitycommand.*;
import com.app.console.postcommand.AddPostCommand;
import com.app.console.postcommand.DeletePostCommand;
import com.app.console.postcommand.EditPostCommand;
import com.app.console.postcommand.ListPostsCommand;
import com.app.console.usercommand.LogoutCommand;
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

        commandMap.put("create-community", new CreateCommunityCommand(printer, communityUseCases, reader, userUseCases));
        commandMap.put("list-communities", new ListCommunityCommand(printer, communityUseCases));
        commandMap.put("logout", new LogoutCommand(printer, userUseCases));
        commandMap.put("exit", new ExitCommand(printer));
        commandMap.put("delete-community", new DeleteCommunityCommand(printer, communityUseCases, reader, userUseCases));
        commandMap.put("edit-community", new EditCommunityCommand(printer, communityUseCases, reader, userUseCases));
        commandMap.put("exit-community", new ExitCommunityCommand(printer, communityUseCases, userUseCases, reader));
        commandMap.put("find-community", new FindCommunityCommand(printer, communityUseCases, reader));
        commandMap.put("join-community", new JoinCommunityCommand(printer, communityUseCases, userUseCases, reader));
        commandMap.put("edit-comment", new EditCommentCommand(printer, commentUseCases, reader, postUseCases, userUseCases, communityUseCases));
        commandMap.put("delete-comment", new DeleteCommentCommand(printer, commentUseCases, reader, communityUseCases, userUseCases));
        commandMap.put("add-comment", new CreateCommentCommand(printer, commentUseCases, communityUseCases, reader, userUseCases));
        commandMap.put("help", new HelpCommand(printer));
        commandMap.put("h", new HelpCommand(printer));
        commandMap.put("add-post", new AddPostCommand(printer, postUseCases, userUseCases, communityUseCases, reader));
        commandMap.put("posts-feed", new PostsFeedCommand(printer, postUseCases, userUseCases));
        commandMap.put("list-posts", new ListPostsCommand(printer, postUseCases, communityUseCases, reader));
        commandMap.put("delete-post", new DeletePostCommand(printer, postUseCases, reader, userUseCases));
        commandMap.put("edit-post", new EditPostCommand(printer, postUseCases, reader, userUseCases));
        commandMap.put("list-comments", new ListCommentCommand(printer,commentUseCases, reader, communityUseCases, userUseCases, postUseCases));

        commandMap.put("0", new ExitCommand(printer));
        commandMap.put("1", new ListCommunityCommand(printer, communityUseCases));
        commandMap.put("2", new FindCommunityCommand(printer, communityUseCases, reader));
        commandMap.put("3", new CreateCommunityCommand(printer, communityUseCases, reader, userUseCases));
        commandMap.put("4", new JoinCommunityCommand(printer, communityUseCases, userUseCases, reader));
        commandMap.put("5", new ExitCommunityCommand(printer, communityUseCases, userUseCases, reader));
        commandMap.put("6", new EditCommunityCommand(printer, communityUseCases, reader, userUseCases));
        commandMap.put("7", new DeleteCommunityCommand(printer, communityUseCases, reader, userUseCases));
        commandMap.put("8", new PostsFeedCommand(printer, postUseCases, userUseCases));
        commandMap.put("9", new ListPostsCommand(printer, postUseCases, communityUseCases, reader));
        commandMap.put("10", new AddPostCommand(printer, postUseCases, userUseCases, communityUseCases, reader));
        commandMap.put("11", new EditPostCommand(printer, postUseCases, reader,  userUseCases));
        commandMap.put("12", new DeletePostCommand(printer, postUseCases, reader, userUseCases));
        commandMap.put("13", new ListCommentCommand(printer,commentUseCases, reader, communityUseCases, userUseCases, postUseCases));
        commandMap.put("14", new CreateCommentCommand(printer, commentUseCases, communityUseCases, reader, userUseCases));
        commandMap.put("15", new EditCommentCommand(printer, commentUseCases, reader, postUseCases, userUseCases, communityUseCases));
        commandMap.put("16", new DeleteCommentCommand(printer, commentUseCases, reader, communityUseCases, userUseCases));
        commandMap.put("17", new HelpCommand(printer));
        commandMap.put("18", new LogoutCommand(printer, userUseCases));

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
                printer.printHomeMenu(); // print the menu before taking input, before any command
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