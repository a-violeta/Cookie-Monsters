package com.app.console;


public class HelpCommand extends Command {

    HelpCommand(ConsolePrinter consolePrinter) {
        super(consolePrinter);
    }

    @Override
    public void execute(String[] args) {

        consolePrinter.printHelp();
    }
}