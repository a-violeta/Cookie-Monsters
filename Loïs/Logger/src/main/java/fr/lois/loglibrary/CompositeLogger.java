package fr.lois.loglibrary;

import java.util.Arrays;
import java.util.List;

public class CompositeLogger implements Logger {
    private List<Logger> loggers;

    // Accepte un nombre variable de loggers en paramètre
    public CompositeLogger(Logger... loggers) {
        this.loggers = Arrays.asList(loggers);
    }

    @Override
    public void log(LogLevel logLevel, String message) {
        // Appelle la méthode log de chaque logger de la liste
        for (Logger logger : loggers) {
            logger.log(logLevel, message);
        }
    }
}