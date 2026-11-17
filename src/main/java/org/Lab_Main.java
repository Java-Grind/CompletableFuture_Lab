package org;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;

public class Lab_Main {
    public static void main(String[] args) {
        System.out.println("Starting Lab Main");
        Instant start = Instant.now();

        CompletableFuture<String> user = CompletableFuture.supplyAsync(
                () -> fakeRequest("user", 500)
        );
        CompletableFuture<String> orders = CompletableFuture.supplyAsync(
                () -> fakeRequest("orders", 700)
        );

        String dashboard = user
                .thenCombine(orders, (userResult, ordersResult) -> userResult + " + " + ordersResult)
                .thenApply(result -> "Dashboard: " + result)
                .join();

        System.out.println(dashboard);
        System.out.println("Done in " + Duration.between(start, Instant.now()));
    }

    private static String fakeRequest(String name, long delayMillis) {
        try {
            Thread.sleep(delayMillis);
            return name + " loaded";
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Request was interrupted", exception);
        }
    }
}
