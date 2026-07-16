import Controller.AuthService;
import Controller.ConsoleReader;
import Controller.InputParser;
import View.ConsolePrinter;
import repository.PostRepository;

public class Main {
    public static void main(String[] args) {

        ConsoleReader consoleReader = new ConsoleReader();

        InputParser inputParser = new InputParser(consoleReader);

        inputParser.startListening();
    }
}