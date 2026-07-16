package Model;

public class HelpCommand implements Command {
    @Override
    public void execute(String[] args){
        System.out.println("--This is the Help Page for Reddit-CLI--");
        System.out.println("post; p; Allow the User to Post in a Community / Usage : post [Title] [Body] [Community] ");
    }
}
