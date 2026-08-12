package com.app.console.clicommand;

import com.app.console.core.Command;
import com.app.console.core.ConsolePrinter;

public class ExitCommand extends Command {

    public ExitCommand(ConsolePrinter consolePrinter) {
        super(consolePrinter);
    }

    @Override
    public void execute(String[] args) {
        consolePrinter.printGoodbye();
        System.exit(0);
    }
}