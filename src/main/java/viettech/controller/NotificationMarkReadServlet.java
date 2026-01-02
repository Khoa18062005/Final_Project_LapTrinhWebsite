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

@WebServlet("/profile/notifications/mark-read")
public class NotificationMarkReadServlet extends HttpServlet {

    private final NotificationService notificationService = new NotificationService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("📌 ===== MARK NOTIFICATION READ DEBUG =====");

        // Check login
        User user = (User) SessionUtil.getAttribute(request, "user");
        if (user == null) {
            System.out.println("❌ User not logged in");
            SessionUtil.setErrorMessage(request, "Vui lòng đăng nhập!");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        int userId = user.getUserId();
        System.out.println("👤 User ID: " + userId);

        try {
            // Lấy tham số từ form
            String notificationIdParam = request.getParameter("notificationId");
            String markAllParam = request.getParameter("markAll");

            System.out.println("📝 Form Data:");
            System.out.println("  - Mark All: " + markAllParam);
            System.out.println("  - Notification ID: " + notificationIdParam);

            // Tạo DTO từ request parameters
            NotificationReadDTO dto = NotificationService.createReadDTOFromRequest(
                    userId, notificationIdParam, markAllParam
            );

            if (dto == null) {
                System.out.println("❌ Failed to create DTO");
                SessionUtil.setErrorMessage(request, "Dữ liệu không hợp lệ!");
                response.sendRedirect(request.getContextPath() + "/profile/notifications");
                return;
            }

            // Gọi service với DTO
            boolean success = notificationService.markAsRead(dto);

            if (success) {
                System.out.println("✅ Mark read operation successful");
                SessionUtil.setSuccessMessage(request, dto.isMarkAll() ?
                        "Đã đánh dấu tất cả thông báo là đã đọc!" :
                        "Đã đánh dấu thông báo là đã đọc!");
            } else {
                System.out.println("❌ Mark read operation failed");
                SessionUtil.setErrorMessage(request, "Có lỗi xảy ra khi đánh dấu đã đọc!");
            }

        } catch (Exception e) {
            System.err.println("❌ ERROR in NotificationMarkReadServlet:");
            e.printStackTrace();
            SessionUtil.setErrorMessage(request, "Có lỗi xảy ra: " + e.getMessage());
        }

        // Redirect về trang thông báo
        response.sendRedirect(request.getContextPath() + "/profile/notifications");
    }
}