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
        userUseCases.logout();
        consolePrinter.printSuccess("Logged out successfully! Returning to login menu...");
    }
}