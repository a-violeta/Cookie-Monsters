import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        AuthService authService = new AuthService(); // On initialise notre service

        // Petit jeu de données de test (pour ne pas devoir s'inscrire à chaque redémarrage)
        authService.register("Lois", "1234");

        System.out.println("--- Welcome to Mini-Reddit CLI ---");

        while (true) {
            System.out.println("\nChoose an option:");
            System.out.println("1. Register (Créer un compte)");
            System.out.println("2. Login (Se connecter)");
            System.out.println("3. Exit (Quitter)");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consomme le retour à la ligne restant

            if (choice == 1) {
                System.out.print("Enter new username: ");
                String user = scanner.nextLine();
                System.out.print("Enter new password: ");
                String pass = scanner.nextLine();

                if (authService.register(user, pass)) {
                    System.out.println("Account created successfully!");
                } else {
                    System.out.println("Error: Username already exists.");
                }

            } else if (choice == 2) {
                System.out.print("Enter username: ");
                String user = scanner.nextLine();
                System.out.print("Enter password: ");
                String pass = scanner.nextLine();

                if (authService.login(user, pass)) {
                    System.out.println("Connection Valid, Welcome " + authService.getCurrentUser().getUsername() + " !");
                    // Ici, tu pourrais rediriger vers le menu des subreddits/posts plus tard
                } else {
                    System.out.println("Incorrect Username or Password!");
                }

            } else if (choice == 3) {
                System.out.println("Goodbye!");
                break; // Arrête la boucle while et ferme le programme
            } else {
                System.out.println("Invalid option.");
            }
        }
        scanner.close();
    }
}