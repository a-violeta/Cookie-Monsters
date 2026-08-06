package com.app.service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class AsyncLoggerService {

    private final BlockingQueue<String> logQueue = new LinkedBlockingQueue<>();

    private volatile boolean isRunning = true;

    private Thread loggerThread;

    @PostConstruct
    public void initLoggerThread() {
        loggerThread = new Thread(() -> {
            // keep running as long as the app is alive, OR if there are still messages left to print
            while (isRunning || !logQueue.isEmpty()) {
                try {
                    // take() will pause (block) the logger thread efficiently if the queue is empty
                    String message = logQueue.take();
                    writeLog(message);
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
        logQueue.offer(formattedMessage);
    }

    public void logError(String message) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String formattedMessage = String.format("[%s] ERROR: %s", timestamp, message);
        logQueue.offer(formattedMessage);
    }

    private void writeLog(String message) {
        System.out.println(message + " | (Handled by " + Thread.currentThread().getName() + ")");
    }

    @PreDestroy
    public void shutdownLogger() {
        isRunning = false;
        loggerThread.interrupt();
    }
}