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
import com.app.service.CommentAbstract;
import com.app.service.CommunityAbstract;
import com.app.service.PostAbstract;
import com.app.service.UserAbstract;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputParser {

    private final UserAbstract userAbstract;

    private final ConsoleReader reader;
    private final ConsolePrinter printer;
    private final Map<String, Command> commandMap = new HashMap<>();

    public InputParser(ConsoleReader reader, ConsolePrinter printer, CommunityAbstract communityAbstract, CommentAbstract commentAbstract, PostAbstract postAbstract, UserAbstract userAbstract) {
        this.reader = reader;
        this.printer = printer;
        this.userAbstract = userAbstract;

        commandMap.put("create-community", new CreateCommunityCommand(printer, communityAbstract, reader, userAbstract));
        commandMap.put("list-communities", new ListCommunityCommand(printer, communityAbstract));
        commandMap.put("logout", new LogoutCommand(printer, userAbstract));
        commandMap.put("exit", new ExitCommand(printer));
        commandMap.put("delete-community", new DeleteCommunityCommand(printer, communityAbstract, reader, userAbstract));
        commandMap.put("edit-community", new EditCommunityCommand(printer, communityAbstract, reader, userAbstract));
        commandMap.put("exit-community", new ExitCommunityCommand(printer, communityAbstract, userAbstract, reader));
        commandMap.put("find-community", new FindCommunityCommand(printer, communityAbstract, reader));
        commandMap.put("join-community", new JoinCommunityCommand(printer, communityAbstract, userAbstract, reader));
        commandMap.put("edit-comment", new EditCommentCommand(printer, commentAbstract, reader, postAbstract, userAbstract, communityAbstract));
        commandMap.put("delete-comment", new DeleteCommentCommand(printer, commentAbstract, reader, communityAbstract, userAbstract));
        commandMap.put("add-comment", new CreateCommentCommand(printer, commentAbstract, communityAbstract, reader, userAbstract));
        commandMap.put("help", new HelpCommand(printer));
        commandMap.put("h", new HelpCommand(printer));
        commandMap.put("add-post", new AddPostCommand(printer, postAbstract, userAbstract, communityAbstract, reader));
        commandMap.put("posts-feed", new PostsFeedCommand(printer, postAbstract, userAbstract));
        commandMap.put("list-posts", new ListPostsCommand(printer, postAbstract, communityAbstract, reader));
        commandMap.put("delete-post", new DeletePostCommand(printer, postAbstract, reader, userAbstract));
        commandMap.put("edit-post", new EditPostCommand(printer, postAbstract, reader, userAbstract));
        commandMap.put("list-comments", new ListCommentCommand(printer, commentAbstract, reader, communityAbstract, userAbstract, postAbstract));

        commandMap.put("0", new ExitCommand(printer));
        commandMap.put("1", new ListCommunityCommand(printer, communityAbstract));
        commandMap.put("2", new FindCommunityCommand(printer, communityAbstract, reader));
        commandMap.put("3", new CreateCommunityCommand(printer, communityAbstract, reader, userAbstract));
        commandMap.put("4", new JoinCommunityCommand(printer, communityAbstract, userAbstract, reader));
        commandMap.put("5", new ExitCommunityCommand(printer, communityAbstract, userAbstract, reader));
        commandMap.put("6", new EditCommunityCommand(printer, communityAbstract, reader, userAbstract));
        commandMap.put("7", new DeleteCommunityCommand(printer, communityAbstract, reader, userAbstract));
        commandMap.put("8", new PostsFeedCommand(printer, postAbstract, userAbstract));
        commandMap.put("9", new ListPostsCommand(printer, postAbstract, communityAbstract, reader));
        commandMap.put("10", new AddPostCommand(printer, postAbstract, userAbstract, communityAbstract, reader));
        commandMap.put("11", new EditPostCommand(printer, postAbstract, reader, userAbstract));
        commandMap.put("12", new DeletePostCommand(printer, postAbstract, reader, userAbstract));
        commandMap.put("13", new ListCommentCommand(printer, commentAbstract, reader, communityAbstract, userAbstract, postAbstract));
        commandMap.put("14", new CreateCommentCommand(printer, commentAbstract, communityAbstract, reader, userAbstract));
        commandMap.put("15", new EditCommentCommand(printer, commentAbstract, reader, postAbstract, userAbstract, communityAbstract));
        commandMap.put("16", new DeleteCommentCommand(printer, commentAbstract, reader, communityAbstract, userAbstract));
        commandMap.put("17", new HelpCommand(printer));
        commandMap.put("18", new LogoutCommand(printer, userAbstract));

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
        while(userAbstract.getLoggedInUser() != null) {
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