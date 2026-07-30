package com.app.console;

import java.io.Console;
import java.util.Scanner;

public class ConsoleReader {

    private final Scanner scanner;

    public ConsoleReader() {
        // Initialize the Scanner
        this.scanner = new Scanner(System.in);
    }

    public void cliPrompt() {
        System.out.print("Reddit-CLI> ");
    }

    public String readLine() {
        if (scanner.hasNextLine()) {
            // Read the Next Line provided by the user, The trim() method removes whitespace from both ends of a string.
            return scanner.nextLine().trim();
        }
        // return "" to ensure the program does not crash when nothing is read by the scanner
        return "";
    }

    public String readSecret() {
        Console console = System.console();

        // When using a real terminal not from an IDE, it will hide the password
        if (console != null) {
            char[] passwordChars = console.readPassword();
            return new String(passwordChars);
        }
        // Inside Intellij it keep the same function has readLine()
        else {
            if (scanner.hasNextLine()) {
                return scanner.nextLine().trim();
            }
            return "";
        }
    }
}
