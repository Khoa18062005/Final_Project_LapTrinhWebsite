package viettech.controller;

import viettech.entity.user.User;
import viettech.service.NotificationService;
import viettech.util.SessionUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@WebServlet("/api/notifications/unread-count")
public class NotificationUnreadCountApiServlet extends HttpServlet {

    private final NotificationService notificationService = new NotificationService();
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> result = new HashMap<>();

        try {
            // Check login
            User user = (User) SessionUtil.getAttribute(request, "user");
            if (user == null) {
                result.put("success", false);
                result.put("message", "User not logged in");
                result.put("count", 0);
            } else {
                int unreadCount = notificationService.getUnreadCountByUserId(user.getUserId());
                result.put("success", true);
                result.put("count", unreadCount);
                result.put("message", "Success");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Error: " + e.getMessage());
            result.put("count", 0);
        }

        String jsonResponse = gson.toJson(result);
        PrintWriter out = response.getWriter();
        out.print(jsonResponse);
        out.flush();
    }
}