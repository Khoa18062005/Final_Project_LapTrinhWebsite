package viettech.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import viettech.entity.Notification;
import viettech.entity.user.Admin;
import viettech.entity.user.Customer;
import viettech.entity.user.User;
import viettech.service.NotificationService;
import viettech.service.UserService;
import viettech.util.EmailUtilBrevo;
import viettech.util.NotificationTemplateUtil;
import viettech.util.SessionUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/contact")
public class ContactServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(ContactServlet.class);
    private final NotificationService notificationService = new NotificationService();
    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Forward to contact page
        request.getRequestDispatcher("/WEB-INF/views/contact.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        logger.info("========================================");
        logger.info("🚀 ContactServlet.doPost() CALLED");
        logger.info("========================================");

        // ========== NHẬN DỮ LIỆU FORM ==========
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String subject = request.getParameter("subject");
        String message = request.getParameter("message");

        logger.info("📝 Contact Form Data:");
        logger.info("   - Name: {}", fullName);
        logger.info("   - Email: {}", email);
        logger.info("   - Phone: {}", phone);
        logger.info("   - Subject: {}", subject);

        // ========== VALIDATE ==========
        if (fullName == null || fullName.trim().isEmpty() ||
                email == null || email.trim().isEmpty() ||
                phone == null || phone.trim().isEmpty() ||
                subject == null || subject.trim().isEmpty() ||
                message == null || message.trim().isEmpty()) {

            logger.error("❌ Validation failed: Missing required fields");
            SessionUtil.setErrorMessage(request, "Vui lòng điền đầy đủ thông tin!");
            response.sendRedirect(request.getContextPath() + "/contact");
            return;
        }

        logger.info("✅ Validation passed");

        try {
            // ========== TÌM KIẾM USER THEO EMAIL TRONG DATABASE ==========
            logger.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            logger.info("🔍 CHECKING: Does email exist in database?");
            logger.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

            Customer customer = userService.findCustomerByEmail(email);

            boolean isRegisteredUser = (customer != null);
            Integer userId = null;

            if (isRegisteredUser) {
                userId = customer.getUserId();
                logger.info("✅ EMAIL FOUND in database!");
                logger.info("   - User ID: {}", userId);
                logger.info("   - Username: {}", customer.getUsername());
                logger.info("   - Full Name: {} {}", customer.getFirstName(), customer.getLastName());
                logger.info("   → This is a REGISTERED user");
            } else {
                logger.info("ℹ️ EMAIL NOT FOUND in database");
                logger.info("   - Email: {}", email);
                logger.info("   → This is a GUEST user (not registered)");
            }

            // ========== 1. TẠO THÔNG BÁO CHO USER (CHỈ NẾU EMAIL TỒN TẠI) ==========
            if (isRegisteredUser) {
                logger.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
                logger.info("📨 STEP 1: Creating USER notification...");
                logger.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

                Notification userNotification = NotificationTemplateUtil.createContactUserNotification(
                        userId,
                        fullName,
                        subject
                );

                boolean userNotifSuccess = notificationService.createNotification(userNotification);
                if (userNotifSuccess) {
                    logger.info("✅ User notification saved to database");
                    logger.info("   - Notification will appear in user's notification center");
                } else {
                    logger.warn("⚠️ Failed to save user notification");
                }
            } else {
                logger.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
                logger.info("⏭️ STEP 1: SKIPPED - Email not in database");
                logger.info("   (Guest users don't have notification center)");
                logger.info("   (Will send confirmation email instead)");
                logger.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            }

            // ========== 2. TẠO THÔNG BÁO CHO TẤT CẢ ADMIN (LUÔN LUÔN THỰC HIỆN) ==========
            logger.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            logger.info("📨 STEP 2: Creating ADMIN notifications...");
            logger.info("   (This happens for both registered and guest users)");
            logger.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

            List<Admin> admins = userService.findAllAdmins();
            logger.info("Found {} admin(s)", admins.size());

            if (admins.isEmpty()) {
                logger.warn("⚠️ No admins found in database!");
            }

            int adminNotifCount = 0;
            for (Admin admin : admins) {
                Notification adminNotification = NotificationTemplateUtil.createContactAdminNotification(
                        admin.getUserId(),
                        fullName,
                        email,
                        phone,
                        subject,
                        message
                );

                boolean adminNotifSuccess = notificationService.createNotification(adminNotification);
                if (adminNotifSuccess) {
                    adminNotifCount++;
                    logger.info("   ✅ Notification created for admin ID={}", admin.getUserId());
                } else {
                    logger.error("   ❌ Failed for admin ID={}", admin.getUserId());
                }
            }

            logger.info("✅ Created {}/{} admin notifications", adminNotifCount, admins.size());

            // ========== 3. GỬI EMAIL XÁC NHẬN (LUÔN LUÔN THỰC HIỆN) ==========
            logger.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            logger.info("📧 STEP 3: Sending confirmation email...");
            logger.info("   (This happens for both registered and guest users)");
            logger.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            logger.info("   - To: {}", email);
            logger.info("   - Name: {}", fullName);

            boolean emailSuccess = EmailUtilBrevo.sendContactConfirmation(
                    email,
                    fullName,
                    subject,
                    message
            );

            if (emailSuccess) {
                logger.info("✅ Confirmation email sent successfully");
            } else {
                logger.warn("⚠️ Failed to send confirmation email");
            }

            // ========== 4. THÔNG BÁO THÀNH CÔNG ==========
            logger.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            logger.info("✅ CONTACT FORM PROCESSING COMPLETED");
            logger.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            logger.info("Summary:");
            logger.info("   - User Type: {}", isRegisteredUser ? "✅ Registered User" : "ℹ️ Guest User");
            if (isRegisteredUser) {
                logger.info("   - User ID: {}", userId);
            }
            logger.info("   - User notification: {}", isRegisteredUser ? "✅ Created" : "⏭️ Skipped (guest)");
            logger.info("   - Admin notifications: ✅ Created ({}/{})", adminNotifCount, admins.size());
            logger.info("   - Confirmation email: {}", emailSuccess ? "✅ Sent" : "❌ Failed");
            logger.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

            SessionUtil.setSuccessMessage(request,
                    "Cảm ơn bạn đã liên hệ! Chúng tôi sẽ phản hồi trong vòng 24 giờ. Vui lòng kiểm tra email.");
            response.sendRedirect(request.getContextPath() + "/");

        } catch (Exception e) {
            logger.error("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            logger.error("💥 EXCEPTION IN CONTACT SERVLET");
            logger.error("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            logger.error("Exception type: {}", e.getClass().getName());
            logger.error("Exception message: {}", e.getMessage());
            logger.error("Full stack trace:", e);

            SessionUtil.setErrorMessage(request, "Có lỗi xảy ra khi gửi tin nhắn. Vui lòng thử lại sau!");
            response.sendRedirect(request.getContextPath() + "/contact");
        }
    }
}
