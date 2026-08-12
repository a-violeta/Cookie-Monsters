package com.app.console.usercommand;

import com.app.console.core.Command;
import com.app.console.core.ConsolePrinter;
import com.app.console.core.ConsoleReader;
import com.app.service.UserAbstract;

public class EditProfileCommand extends Command {
    private final UserAbstract userAbstract;
    private final ConsoleReader consoleReader;

    public EditProfileCommand(ConsolePrinter consolePrinter, UserAbstract userAbstract, ConsoleReader consoleReader) {
        super(consolePrinter);
        this.userAbstract = userAbstract;
        this.consoleReader = consoleReader;
    }

    @Override
    public void execute(String[] args) {
        if (args.length >= 1) {
            consolePrinter.printError("Too Many Arguments");
            return;
        }

        try {
            String currentUsername = userAbstract.getLoggedInUser().getUsername();

            consolePrinter.printPrompt("New display name (leave blank to keep current)");
            String displayName = consoleReader.readLine();
            if (displayName != null && displayName.isBlank()) {
                displayName = null;
            }

            userAbstract.updateProfile(currentUsername, displayName, null);
            consolePrinter.printSuccess("Profile updated successfully!");
        } catch (Exception e) {
            consolePrinter.printError(e.getMessage());
        }
    }
}
