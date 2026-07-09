// Main Class for usage of the Logger
public class Main {
    public static void main() {

        // Configuration : tu choisis comment tu veux logger pour toute l'app
        Logger configBoth = new CompositeLogger(
                new ConsoleLogger("CRITICAL"),
                new FileLogger("WARNING")
        );

        LoggerFactory.configure(configBoth);

        // N'importe où ailleurs dans ton code :
        Logger logger = LoggerFactory.getLogger();
        logger.log("Log Test");
    }
}