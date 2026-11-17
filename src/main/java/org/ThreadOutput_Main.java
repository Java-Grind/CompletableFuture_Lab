package org;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadOutput_Main {
    private static final int TASKS = 12;

    public static void main(String[] args) {
        System.out.println("Starting Thread Output Main");

        try (ExecutorService executor = Executors.newFixedThreadPool(3)) {
            CompletableFuture<?>[] futures = new CompletableFuture<?>[TASKS];

            for (int i = 0; i < TASKS; i++) {
                int taskNumber = i;
                futures[i] = CompletableFuture
                        .supplyAsync(() -> {
                            System.out.printf("Task %02d: load    — %s%n", taskNumber, Thread.currentThread());
                            return "data-" + taskNumber;
                        }, executor)
                        .thenApply(data -> {
                            System.out.printf("Task %02d: convert — %s%n", taskNumber, Thread.currentThread());
                            return data.toUpperCase();
                        })
                        .thenAccept(data -> System.out.printf("Task %02d: result  — %s%n", taskNumber, data));
            }

            CompletableFuture.allOf(futures).join();
        }
    }
}
