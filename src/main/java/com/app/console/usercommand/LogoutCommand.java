package com.app.console.usercommand;

import com.app.console.core.Command;
import com.app.console.core.ConsolePrinter;
import com.app.service.UserAbstract;

public class LogoutCommand extends Command {

    private final UserAbstract userAbstract;

    public LogoutCommand(ConsolePrinter consolePrinter, UserAbstract userAbstract) {
        super(consolePrinter);
        this.userAbstract = userAbstract;
    }

    @Override
    public void execute(String[] args) {

        if (args.length > 0) {
            consolePrinter.printError("Too Many Arguments");
            consolePrinter.printExplanation("logout");
            return;
        }

        userAbstract.logout();
        consolePrinter.printSuccess("Logged out successfully! Returning to login menu...");
    }
}