package viettech.service;

import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;

public class ChatBotService {

    private static ChatBotService instance;
    private SqlAssistant assistant;
    private boolean initialized = false;

    private ChatBotService() {
        initialize();
    }

    public static synchronized ChatBotService getInstance() {
        if (instance == null) {
            instance = new ChatBotService();
        }
        return instance;
    }

    private void initialize() {
        try {
            String apiKey = System.getenv("GROQ_API_KEY");
            if (apiKey == null || apiKey.isEmpty()) {
                System.err.println("WARNING: GROQ_API_KEY environment variable is not set!");
                return;
            }

            OpenAiChatModel model = OpenAiChatModel.builder()
                    .baseUrl("https://api.groq.com/openai/v1")
                    .apiKey(apiKey)
                    .modelName("llama-3.3-70b-versatile")
                    .temperature(0.0)
                    .build();

            this.assistant = AiServices.builder(SqlAssistant.class)
                    .chatModel(model)
                    .tools(new DatabaseTool())
                    .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
                    .build();

            this.initialized = true;
            System.out.println("ChatBotService initialized successfully!");
        } catch (Exception e) {
            System.err.println("Failed to initialize ChatBotService: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean isInitialized() {
        return initialized;
    }

    public String chat(String question) {
        if (!initialized || assistant == null) {
            return "Chatbot chưa được khởi tạo. Vui lòng kiểm tra cấu hình API key.";
        }

        if (question == null || question.trim().isEmpty()) {
            return "Vui lòng nhập câu hỏi.";
        }

        try {
            return assistant.chat(question);
        } catch (Exception e) {
            e.printStackTrace();
            return "Xin lỗi, đã có lỗi xảy ra khi xử lý câu hỏi của bạn: " + e.getMessage();
        }
    }
}
