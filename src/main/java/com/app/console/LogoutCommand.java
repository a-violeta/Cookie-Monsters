package com.app.console;

import com.app.service.UserUseCases;

public class LogoutCommand extends Command {

    private final UserUseCases userUseCases;

    public LogoutCommand(ConsolePrinter consolePrinter, UserUseCases userUseCases) {
        super(consolePrinter);
        this.userUseCases = userUseCases;
    }

    @Override
    public void execute(String[] args) {

        if (args.length > 0) {
            consolePrinter.printError("Too Many Arguments");
            consolePrinter.printExplanation("logout");
            return;
        }

        userUseCases.logout();
        consolePrinter.printSuccess("Logged out successfully! Returning to login menu...");
    }
}