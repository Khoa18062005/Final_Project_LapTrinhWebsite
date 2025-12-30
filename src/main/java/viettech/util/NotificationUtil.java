package viettech.util;

import viettech.entity.user.User;
import viettech.entity.Notification;
import viettech.service.NotificationService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class NotificationUtil {

    private static final NotificationService notificationService = new NotificationService();

    public static void addNotificationAttributes(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if (user != null) {
            int unreadCount = notificationService.getUnreadCountByUserId(user.getUserId());
            request.setAttribute("headerUnreadCount", unreadCount);

            List<Map<String, Object>> notifications = notificationService.getNotificationsByUserId(user.getUserId())
                    .stream()
                    .limit(5)
                    .map(notification -> {
                        Map<String, Object> map = new HashMap<>();
                        map.put("notificationId", notification.getNotificationId());
                        map.put("title", notification.getTitle());
                        map.put("message", notification.getMessage());
                        map.put("type", notification.getType());
                        map.put("read", notification.isRead());
                        map.put("createdAt", notification.getCreatedAt());
                        return map;
                    })
                    .collect(Collectors.toList());

            request.setAttribute("headerNotifications", notifications);
        }
    }
}