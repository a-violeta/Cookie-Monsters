package com.app.service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class AsyncLoggerService {

    private static final Logger logger = LoggerFactory.getLogger(AsyncLoggerService.class);

    private record LogEntry(String message, boolean isError) {}

    private final BlockingQueue<LogEntry> logQueue = new LinkedBlockingQueue<>();

    private volatile boolean isRunning = true;

    private Thread loggerThread;

    @PostConstruct
    public void initLoggerThread() {
        loggerThread = new Thread(() -> {
            // keep running as long as the app is alive, OR if there are still messages left to print
            while (isRunning || !logQueue.isEmpty()) {
                try {
                    // take() will pause (block) the logger thread efficiently if the queue is empty
                    LogEntry entry = logQueue.take();
                    writeLog(entry);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        loggerThread.setName("Background-Logger-Thread");

        loggerThread.setDaemon(true);
        loggerThread.start();
    }

    public void logInfo(String message) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String formattedMessage = String.format("[%s] INFO: %s", timestamp, message);

        // offer() inserts the element into the queue instantly without blocking the main thread
        logQueue.offer(new LogEntry(formattedMessage, false));
    }

    public void logError(String message) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String formattedMessage = String.format("[%s] ERROR: %s", timestamp, message);
        logQueue.offer(new LogEntry(formattedMessage, true));
    }

    private void writeLog(LogEntry entry) {
        String output = entry.message() + " | (Handled by " + Thread.currentThread().getName() + ")";

        // routes through SLF4J/Logback so this actually respects application.yml's
        // logging.file.name config (logs/cookie-monsters.log) - System.out.println
        // never wrote to that file regardless of what logging.* was configured to.
        // uses the real log level so ERROR entries are filterable as ERROR,
        // not just INFO lines that happen to contain the word "ERROR".
        if (entry.isError()) {
            logger.error(output);
        } else {
            logger.info(output);
        }
    }

    @PreDestroy
    public void shutdownLogger() {
        isRunning = false;
        loggerThread.interrupt();
    }
}
