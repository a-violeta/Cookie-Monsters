package com.app.console;

public class ExitCommand extends Command {

    public ExitCommand() {
        super();
    }

    @Override
    public void execute(String[] args) {
        System.out.println("Goodbye see you soon !");
        System.exit(0);
    }
}
