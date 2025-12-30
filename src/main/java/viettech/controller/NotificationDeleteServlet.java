package viettech.controller;

import viettech.dto.NotificationDeleteDTO;
import viettech.entity.user.User;
import viettech.service.NotificationService;
import viettech.util.SessionUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
            String notificationIdParam = request.getParameter("notificationId");

            if (notificationIdParam == null || notificationIdParam.trim().isEmpty()) {
                System.out.println("❌ Missing notification ID");
                SessionUtil.setErrorMessage(request, "Thiếu ID thông báo!");
                response.sendRedirect(request.getContextPath() + "/profile/notifications");
                return;
            }

            System.out.println("🗑️ Deleting notification ID: " + notificationIdParam);

            // Tạo DTO từ request parameters
            NotificationDeleteDTO dto = NotificationService.createDeleteDTOFromRequest(
                    userId, notificationIdParam
            );

            if (dto == null) {
                System.out.println("❌ Failed to create DTO");
                SessionUtil.setErrorMessage(request, "ID thông báo không hợp lệ!");
                response.sendRedirect(request.getContextPath() + "/profile/notifications");
                return;
            }

            // Gọi service với DTO
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