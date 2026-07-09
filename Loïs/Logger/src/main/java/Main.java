public class Main {
    public static void main(String[] args) {

        Logger fileLogger = LoggerFactory.getLogger("file");
        Logger consoleLogger = LoggerFactory.getLogger("console");

        fileLogger.log(LogLevel.CRITICAL, "Log Test");
        consoleLogger.log(LogLevel.OK, "Console Log Test");
    }
}