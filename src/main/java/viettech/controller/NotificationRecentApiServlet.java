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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@WebServlet("/api/notifications/recent")
public class NotificationRecentApiServlet extends HttpServlet {

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
                result.put("notifications", new java.util.ArrayList<>());
            } else {
                // Lấy tối đa 5 thông báo gần nhất
                List<Map<String, Object>> notifications = notificationService.getNotificationsByUserId(user.getUserId())
                        .stream()
                        .limit(5)
                        .map(notification -> {
                            Map<String, Object> notificationMap = new HashMap<>();
                            notificationMap.put("notificationId", notification.getNotificationId());
                            notificationMap.put("title", notification.getTitle());
                            notificationMap.put("message", notification.getMessage());
                            notificationMap.put("type", notification.getType());
                            notificationMap.put("read", notification.isRead());
                            notificationMap.put("createdAt", notification.getCreatedAt());
                            notificationMap.put("actionUrl", notification.getActionUrl());
                            return notificationMap;
                        })
                        .collect(Collectors.toList());

                result.put("success", true);
                result.put("notifications", notifications);
                result.put("message", "Success");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Error: " + e.getMessage());
            result.put("notifications", new java.util.ArrayList<>());
        }

        String jsonResponse = gson.toJson(result);
        PrintWriter out = response.getWriter();
        out.print(jsonResponse);
        out.flush();
    }
}