package viettech.controller;

import viettech.entity.user.User;
import viettech.entity.Notification;
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

/**
 * Extended Notification API Servlet - Handles both GET and POST operations
 * GET: recent notifications, unread notifications
 * POST: mark as read, mark all as read
 */
@WebServlet({"/api/notifications/recent", "/notification"})
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
                result.put("data", new HashMap<String, Object>() {{
                    put("notifications", new java.util.ArrayList<>());
                    put("unreadCount", 0);
                }});
                sendJsonResponse(response, result);
                return;
            }

            String action = request.getParameter("action");
            if (action == null) {
                action = "recent"; // Default action for backward compatibility
            }

            switch (action) {
                case "getUnread":
                    handleGetUnread(user.getUserId(), result);
                    break;
                case "getAll":
                    handleGetAll(user.getUserId(), result);
                    break;
                case "recent":
                default:
                    handleRecent(user.getUserId(), result);
                    break;
            }

        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Error: " + e.getMessage());
            result.put("data", new HashMap<String, Object>() {{
                put("notifications", new java.util.ArrayList<>());
                put("unreadCount", 0);
            }});
        }

        sendJsonResponse(response, result);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> result = new HashMap<>();

        try {
            // Check login
            User user = (User) SessionUtil.getAttribute(request, "user");
            if (user == null) {
                result.put("success", false);
                result.put("message", "Unauthorized");
                sendJsonResponse(response, result);
                return;
            }

            String action = request.getParameter("action");
            if (action == null) {
                result.put("success", false);
                result.put("message", "Action parameter is required");
                sendJsonResponse(response, result);
                return;
            }

            switch (action) {
                case "markRead":
                    handleMarkRead(request, user.getUserId(), result);
                    break;
                case "markAllRead":
                    handleMarkAllRead(user.getUserId(), result);
                    break;
                default:
                    result.put("success", false);
                    result.put("message", "Unknown action: " + action);
                    break;
            }

        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Error: " + e.getMessage());
        }

        sendJsonResponse(response, result);
    }

    /**
     * Handle get recent notifications (limit 5)
     */
    private void handleRecent(int userId, Map<String, Object> result) {
        List<Map<String, Object>> notifications = notificationService.getNotificationsByUserId(userId)
                .stream()
                .limit(5)
                .map(this::convertNotificationToMap)
                .collect(Collectors.toList());

        result.put("success", true);
        result.put("notifications", notifications);
        result.put("message", "Success");
    }

    /**
     * Handle get unread notifications
     */
    private void handleGetUnread(int userId, Map<String, Object> result) {
        List<Notification> unreadNotifications = notificationService.getUnreadNotificationsByUserId(userId);
        int unreadCount = notificationService.getUnreadCountByUserId(userId);

        List<Map<String, Object>> notifications = unreadNotifications.stream()
                .map(this::convertNotificationToMap)
                .collect(Collectors.toList());

        Map<String, Object> data = new HashMap<>();
        data.put("notifications", notifications);
        data.put("unreadCount", unreadCount);

        result.put("success", true);
        result.put("message", "Notifications retrieved successfully");
        result.put("data", data);
        result.put("unreadCount", unreadCount); // For backward compatibility
    }

    /**
     * Handle get all notifications
     */
    private void handleGetAll(int userId, Map<String, Object> result) {
        List<Notification> allNotifications = notificationService.getNotificationsByUserId(userId);
        int unreadCount = notificationService.getUnreadCountByUserId(userId);

        List<Map<String, Object>> notifications = allNotifications.stream()
                .map(this::convertNotificationToMap)
                .collect(Collectors.toList());

        Map<String, Object> data = new HashMap<>();
        data.put("notifications", notifications);
        data.put("unreadCount", unreadCount);

        result.put("success", true);
        result.put("message", "Notifications retrieved successfully");
        result.put("data", data);
    }

    /**
     * Handle mark notification as read
     */
    private void handleMarkRead(HttpServletRequest request, int userId, Map<String, Object> result) {
        String notificationIdStr = request.getParameter("notificationId");
        
        if (notificationIdStr == null || notificationIdStr.isEmpty()) {
            result.put("success", false);
            result.put("message", "Notification ID is required");
            return;
        }

        try {
            int notificationId = Integer.parseInt(notificationIdStr);
            
            // Create DTO for mark read operation
            viettech.dto.NotificationReadDTO dto = new viettech.dto.NotificationReadDTO();
            dto.setNotificationId(notificationId);
            dto.setUserId(userId);
            dto.setMarkAll(false);

            boolean success = notificationService.markAsRead(dto);
            
            if (success) {
                result.put("success", true);
                result.put("message", "Notification marked as read");
            } else {
                result.put("success", false);
                result.put("message", "Failed to mark notification as read");
            }
        } catch (NumberFormatException e) {
            result.put("success", false);
            result.put("message", "Invalid notification ID");
        }
    }

    /**
     * Handle mark all notifications as read
     */
    private void handleMarkAllRead(int userId, Map<String, Object> result) {
        // Create DTO for mark all read operation
        viettech.dto.NotificationReadDTO dto = new viettech.dto.NotificationReadDTO();
        dto.setUserId(userId);
        dto.setMarkAll(true);

        boolean success = notificationService.markAsRead(dto);
        
        if (success) {
            result.put("success", true);
            result.put("message", "All notifications marked as read");
        } else {
            result.put("success", false);
            result.put("message", "Failed to mark all notifications as read");
        }
    }

    /**
     * Convert Notification entity to Map for JSON serialization
     */
    private Map<String, Object> convertNotificationToMap(Notification notification) {
        Map<String, Object> notificationMap = new HashMap<>();
        notificationMap.put("notificationId", notification.getNotificationId());
        notificationMap.put("title", notification.getTitle());
        notificationMap.put("message", notification.getMessage());
        notificationMap.put("type", notification.getType());
        notificationMap.put("read", notification.isRead());
        notificationMap.put("createdAt", notification.getCreatedAt());
        notificationMap.put("actionUrl", notification.getActionUrl());
        return notificationMap;
    }

    /**
     * Send JSON response
     */
    private void sendJsonResponse(HttpServletResponse response, Map<String, Object> result) throws IOException {
        String jsonResponse = gson.toJson(result);
        PrintWriter out = response.getWriter();
        out.print(jsonResponse);
        out.flush();
    }
}