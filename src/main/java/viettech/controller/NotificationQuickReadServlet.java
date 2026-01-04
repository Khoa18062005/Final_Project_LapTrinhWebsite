package viettech.controller;

import viettech.dto.NotificationReadDTO;
import viettech.entity.user.User;
import viettech.service.NotificationService;
import viettech.util.SessionUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/notifications/quick-read")
public class NotificationQuickReadServlet extends HttpServlet {

    private final NotificationService notificationService = new NotificationService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Check login
        User user = (User) SessionUtil.getAttribute(request, "user");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        String notificationIdParam = request.getParameter("notificationId");
        String redirect = request.getParameter("redirect");
        
        try {
            if (notificationIdParam != null && !notificationIdParam.trim().isEmpty()) {
                int notificationId = Integer.parseInt(notificationIdParam);
                
                // Tạo DTO và đánh dấu đã đọc
                NotificationReadDTO dto = new NotificationReadDTO(notificationId, false, user.getUserId());
                notificationService.markAsRead(dto);
                
                System.out.println("✅ Quick read notification: " + notificationId);
            }
            
            // Chuyển hướng
            if (redirect != null && !redirect.trim().isEmpty()) {
                if (redirect.equals("profile")) {
                    response.sendRedirect(request.getContextPath() + "/profile/notifications");
                } else {
                    response.sendRedirect(request.getContextPath() + "/" + redirect);
                }
            } else {
                response.sendRedirect(request.getContextPath() + "/profile/notifications");
            }
            
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid notification ID format");
            response.sendRedirect(request.getContextPath() + "/profile/notifications");
        }
    }
}