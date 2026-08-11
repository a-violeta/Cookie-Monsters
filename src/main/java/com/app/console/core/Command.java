package com.app.console.core;

public abstract class Command {

    protected ConsolePrinter consolePrinter;

    protected Command(ConsolePrinter consolePrinter){
        this.consolePrinter=consolePrinter;
    }

    public abstract void execute(String[] args);

}