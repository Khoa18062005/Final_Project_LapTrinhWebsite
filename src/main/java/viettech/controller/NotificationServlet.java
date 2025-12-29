package viettech.controller;
import viettech.dao.VoucherDAO;
import viettech.entity.Notification;
import viettech.entity.user.User;
import viettech.service.NotificationService;
import viettech.util.SessionUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

import java.util.List;

@WebServlet("/profile/notifications")
public class NotificationServlet extends HttpServlet  {

    private final NotificationService notificationService = new NotificationService();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = (User) SessionUtil.getAttribute(request, "user");
        if (user == null) {
            SessionUtil.setErrorMessage(request, "Vui lòng đăng nhập!");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        List<Notification> notifications = notificationService.getNotificationsByUserId(user.getUserId());
        int unreadCount = notificationService.getUnreadCountByUserId(user.getUserId());

        request.setAttribute("notifications", notifications);
        request.setAttribute("unreadCount", unreadCount);
        request.setAttribute("totalNotifications", notifications.size());
        String path = request.getServletPath();
        String page = path.substring(path.lastIndexOf('/') + 1); // Extract "info", "bank", etc.

    // Forward to JSP
        request.getRequestDispatcher("/WEB-INF/views/profile/" + page + ".jsp")
            .forward(request, response);


    }





}
