import java.util.HashMap;
import java.util.Map;

public class LoggerFactory {

    private static Map<String, Logger> registry = new HashMap<>();

    static {
        registry.put("console", new ConsoleLogger("CONSOLE"));
        registry.put("file", new FileLogger("FILE"));
    }

    // Configuration du logger au démarrage de l'application
    public static void register(String name, Logger logger) {
        registry.put(name, logger);
    }

    public static Logger getLogger(String name) {

        Logger foundLogger = registry.get(name);

        if (foundLogger == null) {
            // Un comportement par défaut très clair si on a oublié de le configurer
            return new ConsoleLogger("DEFAULT");
        }
        return foundLogger;
    }
}