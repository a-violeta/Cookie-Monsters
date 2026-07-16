package com.app.console;

import java.util.Scanner;

public class ConsoleReader {

    private final Scanner scanner;

    public ConsoleReader() {
        //Initialize the Scanner
        this.scanner = new Scanner(System.in);
    }

    public void cliPrompt() {
        System.out.print("Reddit-CLI>");
    }

    public String readLine() {
        if (scanner.hasNextLine()) {
            // Read the Next Line provided by the user, The trim() method removes whitespace from both ends of a string.
            return scanner.nextLine().trim();
        }
        // return "" to ensure the program does not crash when nothing is read by the scanner
        return "";
    }
}
