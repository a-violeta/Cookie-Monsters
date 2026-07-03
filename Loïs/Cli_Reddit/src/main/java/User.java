public class User {

        // Attributs de l'objet (chaque utilisateur aura les siens)
        private final String username;
        private final String password;

        // Le "Constructeur" : permet de créer un utilisateur avec un pseudo et un mdp
    public User(String username, String password) {
            this.username = username;
            this.password = password;
        }

        // "Getters" : Méthodes pour récupérer proprement les données privées
        public String getUsername () {
            return username;
        }

        public String getPassword () {
            return password;
        }

}