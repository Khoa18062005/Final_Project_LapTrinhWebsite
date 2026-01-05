package viettech.controller;

import viettech.dto.NotificationDeleteDTO;
import viettech.entity.Notification;
import viettech.entity.user.User;
import viettech.service.NotificationService;
import viettech.util.SessionUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/profile/notifications/delete")
public class NotificationDeleteServlet extends HttpServlet {

    private final NotificationService notificationService = new NotificationService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("🗑️ ===== DELETE NOTIFICATION DEBUG =====");

        // Check login
        User user = (User) SessionUtil.getAttribute(request, "user");
        if (user == null) {
            System.out.println("❌ User not logged in");
            SessionUtil.setErrorMessage(request, "Vui lòng đăng nhập!");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        int userId = user.getUserId();

        try {
            // Kiểm tra xem có phải xóa tất cả không
            String deleteAllParam = request.getParameter("deleteAll");
            if ("true".equalsIgnoreCase(deleteAllParam)) {
                System.out.println("🗑️ DELETE ALL notifications for user: " + userId);

                List<Notification> allNotifications = notificationService.getNotificationsByUserId(userId);
                int deleteCount = 0;

                for (Notification notification : allNotifications) {
                    NotificationDeleteDTO dto = new NotificationDeleteDTO(notification.getNotificationId(), userId);
                    if (notificationService.deleteNotification(dto)) {
                        deleteCount++;
                    }
                }

                System.out.println("✅ Deleted " + deleteCount + " notifications");
                SessionUtil.setSuccessMessage(request, "Đã xóa tất cả " + deleteCount + " thông báo!");
                response.sendRedirect(request.getContextPath() + "/profile/notifications");
                return;
            }

            // Kiểm tra xem có phải xóa nhiều không
            String[] notificationIdsParam = request.getParameterValues("notificationIds");
            if (notificationIdsParam != null && notificationIdsParam.length > 0) {
                System.out.println("🗑️ DELETE MULTIPLE notifications: " + notificationIdsParam.length);

                int deleteCount = 0;
                for (String idStr : notificationIdsParam) {
                    try {
                        int notificationId = Integer.parseInt(idStr);
                        NotificationDeleteDTO dto = new NotificationDeleteDTO(notificationId, userId);
                        if (notificationService.deleteNotification(dto)) {
                            deleteCount++;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("❌ Invalid notification ID: " + idStr);
                    }
                }

                System.out.println("✅ Deleted " + deleteCount + " notifications");
                SessionUtil.setSuccessMessage(request, "Đã xóa " + deleteCount + " thông báo!");
                response.sendRedirect(request.getContextPath() + "/profile/notifications");
                return;
            }

            // Xóa một thông báo (code cũ)
            String notificationIdParam = request.getParameter("notificationId");
            if (notificationIdParam == null || notificationIdParam.trim().isEmpty()) {
                System.out.println("❌ Missing notification ID");
                SessionUtil.setErrorMessage(request, "Thiếu ID thông báo!");
                response.sendRedirect(request.getContextPath() + "/profile/notifications");
                return;
            }

            System.out.println("🗑️ Deleting single notification ID: " + notificationIdParam);

            NotificationDeleteDTO dto = NotificationService.createDeleteDTOFromRequest(
                    userId, notificationIdParam
            );

            if (dto == null) {
                System.out.println("❌ Failed to create DTO");
                SessionUtil.setErrorMessage(request, "ID thông báo không hợp lệ!");
                response.sendRedirect(request.getContextPath() + "/profile/notifications");
                return;
            }

            boolean success = notificationService.deleteNotification(dto);

            if (success) {
                System.out.println("✅ Notification deleted successfully");
                SessionUtil.setSuccessMessage(request, "Đã xóa thông báo!");
            } else {
                System.out.println("❌ Failed to delete notification");
                SessionUtil.setErrorMessage(request, "Không thể xóa thông báo!");
            }

        } catch (Exception e) {
            System.err.println("❌ ERROR in NotificationDeleteServlet:");
            e.printStackTrace();
            SessionUtil.setErrorMessage(request, "Có lỗi xảy ra khi xóa thông báo!");
        }

        response.sendRedirect(request.getContextPath() + "/profile/notifications");
    }
}