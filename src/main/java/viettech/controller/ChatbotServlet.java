package viettech.controller;

import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import viettech.service.DatabaseTool;
import viettech.service.SqlAssistant;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import com.google.gson.JsonObject;

@WebServlet("/admin/chatbot")
public class ChatbotServlet extends HttpServlet {
    private SqlAssistant assistant;

    @Override
    public void init() throws ServletException {
        super.init();
        String apiKey = System.getenv("GROQ_API_KEY");
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
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Hiển thị trang chatbot
        request.getRequestDispatcher("/WEB-INF/views/admin_pages/chatbot.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String message = request.getParameter("message");
        JsonObject jsonResponse = new JsonObject();

        try {
            if (message == null || message.trim().isEmpty()) {
                jsonResponse.addProperty("success", false);
                jsonResponse.addProperty("message", "Vui lòng nhập câu hỏi");
            } else {
                String answer = getAnswer(message);
                jsonResponse.addProperty("success", true);
                jsonResponse.addProperty("message", answer);
            }
        } catch (Exception e) {
            jsonResponse.addProperty("success", false);
            jsonResponse.addProperty("message", "Lỗi xử lý: " + e.getMessage());
        }

        PrintWriter out = response.getWriter();
        out.print(jsonResponse);
        out.flush();
    }

    public String getAnswer(String question) {
        try {
            return assistant.chat(question);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
