package main;

import main.model.User;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // creates test user using keyboard input
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        System.out.print("Enter description: ");
        String description = scanner.nextLine();

        User firstUser = new User(username, password, description);
        System.out.println(firstUser);

        // creates second user using hardcoded parameters and checks id incrementation
        User secondUser = new User("test2", "@test_2!", "second test user");
        System.out.println(secondUser);

    }
}
