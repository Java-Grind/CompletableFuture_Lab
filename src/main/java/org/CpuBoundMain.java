package org;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CpuBoundMain {
    private static final int TASKS = 80;
    private static final int LIMIT = 300_000;
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();

    public static void main(String[] args) {
        System.out.println("Starting CPU Bound Main");
        System.out.println("Available processors: " + CPU_COUNT);

        try (ExecutorService executor = Executors.newFixedThreadPool(CPU_COUNT)) {
            runExperiment("Fixed executor", executor);
        }
        runExperiment("Common pool", null);
    }

    private static void runExperiment(String name, Executor executor) {
        Instant start = Instant.now();
        CompletableFuture<?>[] futures = new CompletableFuture<?>[TASKS];

        for (int i = 0; i < TASKS; i++) {
            futures[i] = executor == null
                    ? CompletableFuture.supplyAsync(() -> countPrimesUpTo(LIMIT))
                    : CompletableFuture.supplyAsync(() -> countPrimesUpTo(LIMIT), executor);
        }

        CompletableFuture.allOf(futures).join();
        System.out.printf(
                "%s: %.2f seconds%n",
                name,
                Duration.between(start, Instant.now()).toMillis() / 1_000.0
        );
    }

    private static int countPrimesUpTo(int limit) {
        int count = 0;

        for (int number = 2; number <= limit; number++) {
            if (isPrime(number)) {
                count++;
            }
        }

        return count;
    }

    private static boolean isPrime(int number) {
        if (number < 2) {
            return false;
        }

        for (int divisor = 2; divisor * divisor <= number; divisor++) {
            if (number % divisor == 0) {
                return false;
            }
        }

        return true;
    }
}
