package com.app.console;

public abstract class Command {

    private ConsolePrinter consolePrinter;

    Command(ConsolePrinter consolePrinter){
        this.consolePrinter=consolePrinter;
    }

    public abstract void execute(String[] args);

}
