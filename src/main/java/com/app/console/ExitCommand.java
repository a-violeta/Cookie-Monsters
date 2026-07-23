package com.app.console;

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
