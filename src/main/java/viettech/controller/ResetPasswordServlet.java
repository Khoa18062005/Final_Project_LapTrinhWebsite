package viettech.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import viettech.entity.Notification;
import viettech.entity.user.Customer;
import viettech.service.NotificationService;
import viettech.service.UserService;
import viettech.util.NotificationTemplateUtil;
import viettech.util.PasswordUtil;
import viettech.util.SessionUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/reset-password")
public class ResetPasswordServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(ResetPasswordServlet.class);
    private final UserService userService = new UserService();
    private final NotificationService notificationService = new NotificationService();  // ← THÊM MỚI

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Kiểm tra đã xác thực OTP chưa
        Boolean otpVerified = (Boolean) SessionUtil.getAttribute(request, "otpVerified");

        if (otpVerified == null || !otpVerified) {
            response.sendRedirect(request.getContextPath() + "/forgot-password");
            return;
        }

        request.getRequestDispatcher("/WEB-INF/views/reset-password.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Kiểm tra đã xác thực OTP chưa
        Boolean otpVerified = (Boolean) SessionUtil.getAttribute(request, "otpVerified");

        if (otpVerified == null || !otpVerified) {
            response.sendRedirect(request.getContextPath() + "/forgot-password");
            return;
        }

        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        // Validate passwords
        if (newPassword == null || confirmPassword == null ||
                newPassword.isEmpty() || confirmPassword.isEmpty()) {
            request.setAttribute("errorMessage", "Vui lòng nhập đầy đủ mật khẩu");
            request.getRequestDispatcher("/WEB-INF/views/reset-password.jsp")
                    .forward(request, response);
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            request.setAttribute("errorMessage", "Mật khẩu xác nhận không khớp");
            request.setAttribute("newPassword", newPassword);
            request.getRequestDispatcher("/WEB-INF/views/reset-password.jsp")
                    .forward(request, response);
            return;
        }

        if (newPassword.length() < 6) {
            request.setAttribute("errorMessage", "Mật khẩu phải có ít nhất 6 ký tự");
            request.setAttribute("newPassword", newPassword);
            request.getRequestDispatcher("/WEB-INF/views/reset-password.jsp")
                    .forward(request, response);
            return;
        }

        // Lấy thông tin từ session
        String email = (String) SessionUtil.getAttribute(request, "forgotEmail");
        Integer userId = (Integer) SessionUtil.getAttribute(request, "forgotUserId");

        if (email == null || userId == null) {
            request.setAttribute("errorMessage", "Phiên làm việc đã hết hạn");
            request.getRequestDispatcher("/WEB-INF/views/reset-password.jsp")
                    .forward(request, response);
            return;
        }

        try {
            // Tìm customer
            Customer customer = userService.findCustomerByEmail(email);
            if (customer == null || customer.getUserId() != userId) {
                request.setAttribute("errorMessage", "Tài khoản không tồn tại");
                request.getRequestDispatcher("/WEB-INF/views/reset-password.jsp")
                        .forward(request, response);
                return;
            }

            // Cập nhật mật khẩu
            String hashedPassword = PasswordUtil.hashPassword(newPassword);
            customer.setPassword(hashedPassword);
            userService.UpdateLastLoginAt(customer);

            // ========== TẠO THÔNG BÁO ĐỔI MẬT KHẨU ==========
            createPasswordResetNotification(customer);

            // Xóa tất cả session liên quan
            SessionUtil.removeAttribute(request, "forgotEmail");
            SessionUtil.removeAttribute(request, "forgotOtp");
            SessionUtil.removeAttribute(request, "forgotOtpTime");
            SessionUtil.removeAttribute(request, "forgotUserId");
            SessionUtil.removeAttribute(request, "otpVerified");

            // Đặt thông báo thành công
            SessionUtil.setSuccessMessage(request,
                    "Đặt lại mật khẩu thành công! Vui lòng đăng nhập với mật khẩu mới.");

            logger.info("✓ Password reset successfully for user: {}", email);

            // Redirect về trang login
            response.sendRedirect(request.getContextPath() + "/login");

        } catch (Exception e) {
            logger.error("✗ Error resetting password for user: {}", email, e);
            request.setAttribute("errorMessage", "Có lỗi xảy ra khi đặt lại mật khẩu");
            request.getRequestDispatcher("/WEB-INF/views/reset-password.jsp")
                    .forward(request, response);
        }
    }

    /**
     * ========== TẠO THÔNG BÁO ĐỔI MẬT KHẨU ==========
     * ← METHOD MỚI
     * Tạo thông báo khi user đổi mật khẩu thành công
     */
    private void createPasswordResetNotification(Customer customer) {
        int userId = customer.getUserId();
        String firstName = customer.getFirstName();
        String lastName = customer.getLastName();

        try {
            logger.debug("Creating password reset notification for user: {}", userId);

            // Tạo notification từ template
            Notification passwordResetNotification = NotificationTemplateUtil.createPasswordResetNotification(
                    userId,
                    firstName,
                    lastName
            );

            // Lưu vào database thông qua service
            boolean success = notificationService.createNotification(passwordResetNotification);

            if (success) {
                logger.info("✓ Password reset notification created for user: {}", userId);
            } else {
                logger.warn("✗ Failed to create password reset notification for user: {}", userId);
            }

        } catch (Exception e) {
            // Không cho lỗi notification ảnh hưởng đến đổi mật khẩu
            logger.error("✗ Failed to create password reset notification for user: {}", userId, e);
        }
    }
}