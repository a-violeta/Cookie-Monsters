public class Main {
    public static void main() {

        // Configuration : tu choisis comment tu veux logger pour toute l'app
        Logger configBoth = new CompositeLogger(
                new ConsoleLogger("DATE-TEST"),
                new FileLogger("DATE-TEST")
        );

        LoggerFactory.configure(configBoth);
        Logger logger = LoggerFactory.getLogger();
        logger.log("Log Test");
    }
}