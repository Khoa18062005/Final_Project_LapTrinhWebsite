package viettech.controller;

import viettech.service.ChatBotService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@WebServlet("/admin/chat")
public class ChatbotServlet extends HttpServlet {

    private ChatBotService chatBotService;

    @Override
    public void init() throws ServletException {
        super.init();
        chatBotService = ChatBotService.getInstance();
        System.out.println("ChatbotServlet initialized, service ready: " + chatBotService.isInitialized());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Enable CORS if needed
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");

        JsonObject jsonResponse = new JsonObject();
        PrintWriter out = response.getWriter();

        try {
            // Read JSON body
            StringBuilder sb = new StringBuilder();
            BufferedReader reader = request.getReader();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            String requestBody = sb.toString();
            String question = null;

            // Parse JSON to get question
            if (!requestBody.isEmpty()) {
                JsonObject jsonRequest = JsonParser.parseString(requestBody).getAsJsonObject();
                if (jsonRequest.has("question")) {
                    question = jsonRequest.get("question").getAsString();
                }
            }

            if (question == null || question.trim().isEmpty()) {
                jsonResponse.addProperty("success", false);
                jsonResponse.addProperty("response", "Vui lòng nhập câu hỏi");
            } else {
                // Call ChatBotService to get answer
                String answer = chatBotService.chat(question);
                jsonResponse.addProperty("success", chatBotService.isInitialized());
                jsonResponse.addProperty("response", answer);
            }
        } catch (Exception e) {
            e.printStackTrace();
            jsonResponse.addProperty("success", false);
            jsonResponse.addProperty("response", "Lỗi xử lý: " + e.getMessage());
        }

        out.print(jsonResponse);
        out.flush();
    }

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Handle CORS preflight request
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setStatus(HttpServletResponse.SC_OK);
    }

}
