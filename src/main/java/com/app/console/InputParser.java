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
        // Add Commands Classes to the map of commands
        //commandMap.put("h", new HelpCommand());
        //commandMap.put("help", new HelpCommand());
        // On enregistre la commande en lui donnant accès au repo et au printer
        //commandMap.put("post", new PostCommand(postRepo, printer));
    }

    Scanner ReadInput = new Scanner(System.in);

    private String[] tokenizeInput(String input) {
        List<String> tokens = new ArrayList<>();

        // 1. Cherche du texte entre guillemets ("...")
        // 2. OU (|) cherche n'importe quel mot sans espace (\S+)
        Pattern pattern = Pattern.compile("\"([^\"]*)\"|(\\S+)");
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            if (matcher.group(1) != null) {
                // On a trouvé des guillemets, on ajoute le texte (sans les guillemets)
                tokens.add(matcher.group(1));
            } else {
                // On a trouvé un mot normal
                tokens.add(matcher.group(2));
            }
        }

        // On convertit la liste en tableau classique pour respecter notre interface Command
        return tokens.toArray(new String[0]);
    }

    public void startListening() {
        while(true) {
            try {
                reader.cliPrompt();
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
