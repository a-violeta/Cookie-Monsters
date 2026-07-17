package com.app.console;

import com.app.service.CommunityService;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputParser {

    private final CommunityService communityService;

    private ConsoleReader reader;
    private Map<String, Command> commandMap = new HashMap<>();
    //PostRepository postRepo = new PostRepository();
    //ConsolePrinter printer = new ConsolePrinter();

    public InputParser(ConsoleReader reader, CommunityService communityService) {
        this.reader = reader;
        this.communityService = communityService;
        commandMap.put("4", new CreateCommunityCommand(communityService));
        commandMap.put("10", new ListCommunityCommand(communityService));
        commandMap.put("0", new ExitCommand(communityService));
        commandMap.put("14", new DeleteCommunityCommand(communityService));
        // Add Commands Classes to the map of commands
    }

    Scanner ReadInput = new Scanner(System.in);

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
        while(true) {
            try {

                reader.cliPrompt(); //Start the Prompt Method

                String input = reader.readLine(); // Read command like this : post (name of the command) 1234 (Other arguments needed for the command)

                String[] parts = tokenizeInput(input); // Splits arguments in an array of Strings

                if (parts.length == 0) continue;

                Command command = commandMap.get(parts[0].toLowerCase());
                // Get the command name to use the execute method of the selected Command
                if (command != null) {
                    // Read the other arguments (Here will be "1234" when input is "post 1234")
                    command.execute(Arrays.copyOfRange(parts, 1, parts.length));
                    // Execute the method execute of the "command" Class
                } else {
                    System.out.println("Unknown Command ");
                }
            } catch (Exception e) {
                System.out.println("An error as occurred during the Input Reading : " + e.getMessage());
            }
        }
    }
}
