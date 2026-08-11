package com.app.console.clicommand;


import com.app.console.core.Command;
import com.app.console.core.ConsolePrinter;

public class HelpCommand extends Command {

    HelpCommand(ConsolePrinter consolePrinter) {
        super(consolePrinter);
    }

    @Override
    public void execute(String[] args) {

        consolePrinter.printHelp();
    }
}