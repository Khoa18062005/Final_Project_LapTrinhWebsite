package viettech.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChatService {

    private static final Logger logger = LoggerFactory.getLogger(ChatService.class);
    private static ChatService instance = new ChatService();
    private static String Endpoint;

    private ChatService() {
        Endpoint = System.getenv("CHAT_ENDPOINT_API");

        System.out.println("=== ChatService Initialization ===");
        System.out.println("CHAT_ENDPOINT_API value: " + Endpoint);
        logger.info("=== ChatService Initialization ===");
        logger.info("CHAT_ENDPOINT_API value: {}", Endpoint);

        if (!isEndpointConfigured()) {
            System.err.println("❌ CHAT_ENDPOINT_API environment variable is NOT SET or INVALID!");
            System.err.println("❌ Chatbot will use fallback responses until CHAT_ENDPOINT_API is configured.");
            System.err.println("❌ To fix: Set environment variable CHAT_ENDPOINT_API=https://your-chat-api-endpoint.com");
            logger.error("❌ CHAT_ENDPOINT_API is NOT SET or INVALID!");
        } else {
            System.out.println("✅ ChatService initialized successfully with endpoint: " + Endpoint);
            logger.info("✅ ChatService initialized successfully with endpoint: {}", Endpoint);
        }
        System.out.println("=================================");
    }

    public static ChatService getInstance() {
        return instance;
    }

    public String getEndpoint() {
        return Endpoint;
    }

    public void setEndpoint(String endpoint) {
        Endpoint = endpoint;
    }

    /**
     * Check if the chat API endpoint is configured and valid
     */
    public boolean isEndpointConfigured() {
        if (Endpoint == null || Endpoint.trim().isEmpty()) {
            return false;
        }
        // Validate URL has proper scheme (http or https)
        String trimmed = Endpoint.trim().toLowerCase();
        return trimmed.startsWith("http://") || trimmed.startsWith("https://");
    }

    public String chat(String question) {
        if (!isEndpointConfigured()) {
            logger.warn("Chat API endpoint not configured or invalid: {}", Endpoint);
            System.err.println("❌ Chat API endpoint not configured or invalid: " + Endpoint);
            return "{\"answer\": \"Xin lỗi, dịch vụ chat AI hiện chưa được cấu hình. Vui lòng liên hệ admin để được hỗ trợ! 😊\"}";
        }

        try {
            HttpClient client = HttpClient.newHttpClient();

            String jsonBody = String.format(
                    "{\"question\": \"%s\"}",
                    question.replace("\"", "\\\"").replace("\n", "\\n")
            );

            String fullUrl = Endpoint.trim() + "/ask";
            System.out.println("📤 Calling chat API: " + fullUrl);
            logger.info("Calling chat API: {}", fullUrl);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(fullUrl))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("📥 Chat API response status: " + response.statusCode());
            logger.info("Chat API response status: {}", response.statusCode());

            return response.body();

        } catch (Exception e) {
            System.err.println("❌ Error calling chat API: " + e.getMessage());
            logger.error("Error calling chat API", e);
            return "{\"answer\": \"Xin lỗi, đã có lỗi xảy ra khi kết nối với AI. Vui lòng thử lại sau! 😅\"}";
        }
    }
}
