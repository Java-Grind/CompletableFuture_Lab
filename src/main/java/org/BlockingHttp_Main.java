package org;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;

public class BlockingHttp_Main {
    private static final int NUMBER_OF_TASKS = 20;
    private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();
    private static final HttpRequest REQUEST = HttpRequest.newBuilder()
            .uri(URI.create("https://postman-echo.com/delay/1"))
            .GET()
            .build();

    public static void main(String[] args) {
        Instant start = Instant.now();
        CompletableFuture<?>[] requests = new CompletableFuture<?>[NUMBER_OF_TASKS];

        for (int i = 0; i < NUMBER_OF_TASKS; i++) {
            int taskNumber = i;
            requests[i] = HTTP_CLIENT.sendAsync(REQUEST, HttpResponse.BodyHandlers.discarding())
                    .thenAccept(response -> System.out.printf(
                            "Task %02d: HTTP %d%n", taskNumber, response.statusCode()
                    ))
                    .exceptionally(exception -> {
                        System.err.printf("Task %02d failed: %s%n", taskNumber, exception.getMessage());
                        return null;
                    });
        }

        CompletableFuture.allOf(requests).join();
        System.out.printf(
                "All requests finished in %.2f seconds%n",
                Duration.between(start, Instant.now()).toMillis() / 1_000.0
        );
    }
}
