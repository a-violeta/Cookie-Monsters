import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AuthService {
    // Une liste pour stocker tous les utilisateurs créés pendant que le programme tourne
    private final List<User> database = new ArrayList<>();
    // Variable pour savoir qui est actuellement connecté
    private User currentUser = null;

    // Méthode pour créer un compte (User Creation)
    public boolean register(String username, String password) {
        // Vérification : est-ce que le pseudo existe déjà ?
        for (User u : database) {
            if (Objects.equals(u.getUsername(), username)) {
                return false; // Échec : le pseudo est déjà pris
            }
        }
        // Si tout est bon, on crée l'objet User et on l'ajoute à notre liste
        User newUser = new User(username, password);
        database.add(newUser);
        return true; // Succès !
    }

    // Méthode pour se connecter (User Login)
    public boolean login(String username, String password) {
        for (User u : database) {
            // Si le pseudo ET le mot de passe correspondent
            if (Objects.equals(u.getUsername(), username) && Objects.equals(u.getPassword(), password)) {
                currentUser = u; // On retient qui est connecté
                return true; // Connexion réussie
            }
        }
        return false; // Identifiants incorrects
    }

    // Permet de récupérer l'utilisateur connecté depuis l'extérieur
    public User getCurrentUser() {
        return currentUser;
    }
}
