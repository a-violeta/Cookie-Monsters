public class LoggerFactory {
    private static Logger instance;

    // Configuration du logger au démarrage de l'application
    public static void configure(Logger logger) {
        instance = logger;
    }

    public static Logger getLogger() {
        if (instance == null) {
            // Un comportement par défaut très clair si on a oublié de le configurer
            instance = new ConsoleLogger("DEFAULT");
        }
        return instance;
    }
}