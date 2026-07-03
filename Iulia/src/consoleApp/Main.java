package consoleApp;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        List<User> applicationUsers = new ArrayList<>();

        while(true){
            System.out.println("1. Create account");
            System.out.println("2. Log into account");
            System.out.println("3. Log out of account");
            System.out.println("4. consoleApp.Post (just text)");
            System.out.println("5. consoleApp.Comment");
            System.out.println("6. Create account");
            System.out.println("7. Create community");
            System.out.println("0. Exit\n");

            System.out.print("Choose option: ");

            int option = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (option) {

                case 1 -> {

                }
                case 2 -> {

                }
                case 3 -> {

                }
                case 4 -> {

                }
                case 5 -> {

                }
                case 6 -> {

                }
                case 7 -> {

                }
                case 0 -> {
                    System.out.println("Goodbye!");
                    return;
                }

                default -> System.out.println("Invalid option.");
            }
        }
    }
}