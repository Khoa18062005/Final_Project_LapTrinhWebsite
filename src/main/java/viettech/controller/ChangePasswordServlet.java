package viettech.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import viettech.dao.CustomerDAO;
import viettech.dto.ChangePassword_dto;
import viettech.entity.Notification;
import viettech.entity.user.Customer;
import viettech.entity.user.User;
import viettech.service.NotificationService;
import viettech.util.NotificationTemplateUtil;
import viettech.util.PasswordUtil;
import viettech.util.SessionUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@WebServlet("/profile/password/change")
public class ChangePasswordServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(ChangePasswordServlet.class);
    private final CustomerDAO customerDAO = new CustomerDAO();
    private final NotificationService notificationService = new NotificationService();  // ← THÊM MỚI

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        logger.info("🔐 ===== CHANGE PASSWORD DEBUG =====");

        // Check login
        User user = (User) SessionUtil.getAttribute(request, "user");
        if (user == null) {
            logger.warn("❌ User not logged in");
            SessionUtil.setErrorMessage(request, "Vui lòng đăng nhập!");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Check if user is Customer
        if (!(user instanceof Customer)) {
            logger.warn("❌ User is not a Customer");
            SessionUtil.setErrorMessage(request, "Chỉ khách hàng mới có thể đổi mật khẩu!");
            response.sendRedirect(request.getContextPath() + "/profile/password");
            return;
        }

        Customer customer = (Customer) user;
        logger.info("👤 Customer: {} (ID: {})", customer.getEmail(), customer.getUserId());

        try {
            // Lấy dữ liệu từ form
            ChangePassword_dto dto = new ChangePassword_dto();
            dto.setCurrentPassword(request.getParameter("currentPassword"));
            dto.setNewPassword(request.getParameter("newPassword"));
            dto.setConfirmPassword(request.getParameter("confirmPassword"));

            logger.debug("📝 Form data received");

            // VALIDATE 1: Kiểm tra dữ liệu đầu vào
            if (!dto.isValid()) {
                logger.warn("❌ Validation failed: Missing fields");
                SessionUtil.setErrorMessage(request, "Vui lòng điền đầy đủ thông tin!");
                response.sendRedirect(request.getContextPath() + "/profile/password");
                return;
            }

            // VALIDATE 2: Kiểm tra mật khẩu mới khớp với xác nhận
            if (!dto.passwordsMatch()) {
                logger.warn("❌ Validation failed: Passwords don't match");
                SessionUtil.setErrorMessage(request, "Mật khẩu mới và xác nhận không khớp!");
                response.sendRedirect(request.getContextPath() + "/profile/password");
                return;
            }

            // VALIDATE 3: Kiểm tra mật khẩu hiện tại đúng không
            if (!PasswordUtil.verifyPassword(dto.getCurrentPassword(), customer.getPassword())) {
                logger.warn("❌ Current password incorrect for user: {}", customer.getEmail());
                SessionUtil.setErrorMessage(request, "Mật khẩu hiện tại không đúng!");
                response.sendRedirect(request.getContextPath() + "/profile/password");
                return;
            }

            // VALIDATE 4: Kiểm tra mật khẩu mới khác mật khẩu cũ
            if (PasswordUtil.verifyPassword(dto.getNewPassword(), customer.getPassword())) {
                logger.warn("❌ New password same as current password");
                SessionUtil.setErrorMessage(request, "Mật khẩu mới phải khác mật khẩu hiện tại!");
                response.sendRedirect(request.getContextPath() + "/profile/password");
                return;
            }

            // ✅ CẬP NHẬT MẬT KHẨU MỚI
            String hashedNewPassword = PasswordUtil.hashPassword(dto.getNewPassword());
            customer.setPassword(hashedNewPassword);
            customer.setUpdatedAt(new Date());

            customerDAO.update(customer);

            // Cập nhật session
            SessionUtil.setAttribute(request, "user", customer);

            // ========== TẠO THÔNG BÁO ĐỔI MẬT KHẨU ==========
            createPasswordChangeNotification(customer);

            logger.info("✅ Password changed successfully for user: {}", customer.getEmail());
            SessionUtil.setSuccessMessage(request, "Đổi mật khẩu thành công! 🎉");
            response.sendRedirect(request.getContextPath() + "/profile/password");

        } catch (Exception e) {
            logger.error("❌ ERROR in ChangePasswordServlet:", e);
            SessionUtil.setErrorMessage(request, "Có lỗi xảy ra khi đổi mật khẩu!");
            response.sendRedirect(request.getContextPath() + "/profile/password");
        }
    }

    /**
     * ========== TẠO THÔNG BÁO ĐỔI MẬT KHẨU ==========
     * Tạo thông báo khi user đổi mật khẩu thành công
     */
    private void createPasswordChangeNotification(Customer customer) {
        int userId = customer.getUserId();
        String firstName = customer.getFirstName();
        String lastName = customer.getLastName();

        try {
            logger.debug("Creating password change notification for user: {}", userId);

            // Tạo notification từ template
            Notification passwordChangeNotification = NotificationTemplateUtil.createPasswordResetNotification(
                    userId,
                    firstName,
                    lastName
            );

            // Lưu vào database thông qua service
            boolean success = notificationService.createNotification(passwordChangeNotification);

            if (success) {
                logger.info("✓ Password change notification created for user: {}", userId);
            } else {
                logger.warn("✗ Failed to create password change notification for user: {}", userId);
            }

        } catch (Exception e) {
            // Không cho lỗi notification ảnh hưởng đến đổi mật khẩu
            logger.error("✗ Failed to create password change notification for user: {}", userId, e);
        }
    }
}