package main.model;

public class HelpCommand implements Command {
    @Override
    public void execute(String[] args){
        System.out.println("--This is the Help Page for Reddit-CLI--");
        System.out.println("Here write help for the commands");
    }
}
