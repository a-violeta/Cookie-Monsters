package fr.lois.loglibrary;

public class ConsoleLogger implements Logger {
    private String prefix;
    public ConsoleLogger(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public void log(LogLevel logLevel, String message) {
        System.out.println(" [" + logLevel + "] " + prefix + " / " + message);
    }
}