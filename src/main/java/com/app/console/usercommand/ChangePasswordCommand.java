package com.app.console.usercommand;

import com.app.console.core.Command;
import com.app.console.core.ConsolePrinter;
import com.app.console.core.ConsoleReader;
import com.app.service.UserUseCases;

public class ChangePasswordCommand extends Command {
    private final UserUseCases userUseCases;
    private final ConsoleReader consoleReader;

    public ChangePasswordCommand(ConsolePrinter consolePrinter, UserUseCases userUseCases, ConsoleReader consoleReader) {
        super(consolePrinter);
        this.userUseCases = userUseCases;
        this.consoleReader = consoleReader;
    }

    @Override
    public void execute(String[] args) {
        if (args.length >= 1) {
            consolePrinter.printError("Too Many Arguments");
            return;
        }

        try {
            String currentUsername = userUseCases.getLoggedInUser().getUsername();

            consolePrinter.printPrompt("Current password");
            String currentPassword = consoleReader.readSecret();

            consolePrinter.printPrompt("New password");
            String newPassword = consoleReader.readSecret();

            consolePrinter.printPrompt("Confirm new password");
            String confirmPassword = consoleReader.readSecret();

            if (!newPassword.equals(confirmPassword)) {
                throw new IllegalArgumentException("New password and confirmation do not match");
            }

            userUseCases.changePassword(currentUsername, currentPassword, newPassword);
            consolePrinter.printSuccess("Password changed successfully!");
        } catch (Exception e) {
            consolePrinter.printError(e.getMessage());
        }
    }
}
