package Model;

public class User {

        // Attributs de l'objet (chaque utilisateur aura les siens)
        private final String username;
        private final String hashPassword;

        // Le "Constructeur" : permet de créer un utilisateur avec un pseudo et un mdp
    public User(String username, String password) {
            this.username = username;
            this.hashPassword = password;
    }

        public String getUsername () {
            return username;
        }

        public String getPassword () {
            return hashPassword;
        }

}