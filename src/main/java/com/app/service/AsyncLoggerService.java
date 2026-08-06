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

    // A thread-safe queue to pass messages between the main app and the logger thread
    private final BlockingQueue<String> logQueue = new LinkedBlockingQueue<>();

    // Volatile ensures changes to this variable are immediately visible to the logger thread
    private volatile boolean isRunning = true;

    private Thread loggerThread;

    @PostConstruct
    public void initLoggerThread() {
        // Initialize the separate thread
        loggerThread = new Thread(() -> {
            // Keep running as long as the app is alive, OR if there are still messages left to print
            while (isRunning || !logQueue.isEmpty()) {
                try {
                    // take() will pause (block) the logger thread efficiently if the queue is empty
                    // It wakes up instantly when the main app adds a message
                    String message = logQueue.take();
                    writeLog(message);
                } catch (InterruptedException e) {
                    // Restore interrupted status
                    Thread.currentThread().interrupt();
                }
            }
        });

        // Name the thread so it is easy to identify in debugging
        loggerThread.setName("Background-Logger-Thread");

        // A daemon thread will not prevent the JVM from shutting down
        loggerThread.setDaemon(true);
        loggerThread.start();
    }

    // Main application threads will call this method
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
        // Here we print it to the console, appending the thread name to prove it runs separately
        System.out.println(message + " | (Handled by " + Thread.currentThread().getName() + ")");
    }

    @PreDestroy
    public void shutdownLogger() {
        // When Spring shuts down, signal the loop to stop
        isRunning = false;
        // Wake up the thread immediately in case it is sleeping/waiting on an empty queue
        loggerThread.interrupt();
    }
}