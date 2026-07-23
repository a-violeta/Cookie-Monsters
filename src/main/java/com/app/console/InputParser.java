package com.app.console;

import com.app.service.CommentUseCases;
import com.app.service.CommunityUseCases;
import com.app.service.PostUseCases;
import com.app.service.UserUseCases; // Decomentat

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
    private Map<String, Command> commandMap = new HashMap<>();

    public InputParser(ConsoleReader reader, ConsolePrinter printer, CommunityUseCases communityUseCases, CommentUseCases commentUseCases, PostUseCases postUseCases, UserUseCases userUseCases) {
        this.reader = reader;
        this.printer = printer;
        this.communityUseCases = communityUseCases;
        this.commentUseCases = commentUseCases;
        this.userUseCases = userUseCases;
        this.postUseCases = postUseCases;

        commandMap.put("4", new CreateCommunityCommand(printer, communityUseCases));
        commandMap.put("10", new ListCommunityCommand(printer, communityUseCases));
        commandMap.put("3", new LogoutCommand(printer, userUseCases));
        commandMap.put("0", new ExitCommand(printer));
        commandMap.put("14", new DeleteCommunityCommand(printer, communityUseCases));
        commandMap.put("18", new EditCommunityCommand(printer, communityUseCases));
        commandMap.put("16", new ExitCommunityCommand(printer, communityUseCases));
        commandMap.put("19", new FindCommunityCommand(printer, communityUseCases));
        commandMap.put("17", new JoinCommunityCommand(printer, communityUseCases));
        commandMap.put("15", new RemovePostFromCommunityCommand(printer, communityUseCases));
        commandMap.put("20", new EditCommentCommand(printer, commentUseCases));
        commandMap.put("13", new DeleteCommentCommand(printer, commentUseCases));
        commandMap.put("5", new CreateCommentCommand(printer, commentUseCases));
        commandMap.put("help", new HelpCommand(printer));
        commandMap.put("h", new HelpCommand(printer));

        commandMap.put("6", new AddPostCommand(printer, postUseCases));
        commandMap.put("8", new ListPostsCommand(printer, postUseCases));
        commandMap.put("12", new DeletePostCommand(printer, postUseCases));
        commandMap.put("21", new EditPostCommand(printer, postUseCases));

        // Add Commands Classes to the map of commands
    }

    //Scanner ReadInput = new Scanner(System.in);

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
        //while user is logged, parser will read commands
        //after logout call userUserCase.logout, the loggerInUser=null and exit while
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
                    System.out.println("Unknown Command ");
                }
            } catch (Exception e) {
                System.out.println("An error as occurred during the Input Reading : " + e.getMessage());
            }
        }
    }
}




