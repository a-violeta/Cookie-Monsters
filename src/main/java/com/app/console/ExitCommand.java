package com.app.console;

public class ExitCommand extends Command {

    public ExitCommand(ConsolePrinter consolePrinter) {
        super(consolePrinter);
    }

    @Override
    public void execute(String[] args) {
        System.out.println("Goodbye see you soon !");
        System.exit(0);
    }
}
