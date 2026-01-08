package viettech.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SentimentAnalysis {

    private static final Logger logger =
            LoggerFactory.getLogger(SentimentAnalysis.class);

    private static final SentimentAnalysis instance = new SentimentAnalysis();
    private static String Endpoint;

    private SentimentAnalysis() {
        Endpoint = System.getenv("CHAT_ENDPOINT_API");

        logger.info("=== SentimentAnalysis Initialization ===");
        logger.info("CHAT_ENDPOINT_API value: {}", Endpoint);

        if (!isEndpointConfigured()) {
            logger.error("❌ CHAT_ENDPOINT_API is NOT SET or INVALID!");
        } else {
            logger.info("✅ SentimentAnalysis initialized with endpoint: {}", Endpoint);
        }
    }

    public static SentimentAnalysis getInstance() {
        return instance;
    }

    public boolean isEndpointConfigured() {
        if (Endpoint == null || Endpoint.trim().isEmpty()) {
            return false;
        }
        String trimmed = Endpoint.trim().toLowerCase();
        return trimmed.startsWith("http://") || trimmed.startsWith("https://");
    }

    /**
     * Call FastAPI /Sentiment
     */
    public String getResult(String sentence) {
        if (!isEndpointConfigured()) {
            return "{\"sentiment\":\"SERVICE_NOT_CONFIGURED\"}";
        }

        try {
            HttpClient client = HttpClient.newHttpClient();

            // JSON MUST match FastAPI schema
            String jsonBody = String.format(
                    "{\"sentence\":\"%s\"}",
                    sentence
                            .replace("\\", "\\\\")
                            .replace("\"", "\\\"")
                            .replace("\n", "\\n")
            );

            String fullUrl = Endpoint.trim() + "/Sentiment";

            logger.info("📤 Calling Sentiment API: {}", fullUrl);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(fullUrl))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            logger.info("📥 Sentiment API status: {}", response.statusCode());
            logger.info("📥 Sentiment API response: {}", response.body());

            return response.body();

        } catch (Exception e) {
            logger.error("❌ Error calling Sentiment API", e);
            return "{\"sentiment\":\"ERROR\"}";
        }
    }
}
