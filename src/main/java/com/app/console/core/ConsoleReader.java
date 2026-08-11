package com.app.console.core;

import java.io.Console;
import java.util.Scanner;

public class ConsoleReader {

    private final Scanner scanner;

    public ConsoleReader() {
        this.scanner = new Scanner(System.in);
    }

    public void cliPrompt() {
        System.out.print("Reddit-CLI> ");
    }

    public String readLine() {
        if (scanner.hasNextLine()) {
            return scanner.nextLine().trim();
        }
        return "";
    }

    public String readSecret() {
        Console console = System.console();

        if (console != null) {
            char[] passwordChars = console.readPassword();
            return new String(passwordChars);
        } else {
            if (scanner.hasNextLine()) {
                return scanner.nextLine().trim();
            }
            return "";
        }
    }
}