package viettech.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import viettech.entity.Notification;
import viettech.entity.user.Admin;
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

@WebServlet("/careers/shipper")
public class ShipperApplicationServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(ShipperApplicationServlet.class);
    private final NotificationService notificationService = new NotificationService();
    private final UserService userService = new UserService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        // ========== KIỂM TRA ĐĂNG NHẬP ==========
        User user = (User) SessionUtil.getAttribute(request, "user");
        if (user == null) {
            SessionUtil.setErrorMessage(request, "Vui lòng đăng nhập để gửi đơn!");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // ========== NHẬN DỮ LIỆU FORM ==========
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String gender = request.getParameter("gender");
        String licenseNumber = request.getParameter("licenseNumber");
        String vehicleType = request.getParameter("vehicleType");
        String vehiclePlate = request.getParameter("vehiclePlate");
        String address = request.getParameter("address");
        String experience = request.getParameter("experience");

        logger.info("📝 Received Shipper application from user: {} ({})", user.getUserId(), email);

        // ========== VALIDATE ==========
        if (licenseNumber == null || licenseNumber.trim().isEmpty() ||
            vehicleType == null || vehicleType.trim().isEmpty() ||
            vehiclePlate == null || vehiclePlate.trim().isEmpty()) {
            SessionUtil.setErrorMessage(request, "Vui lòng điền đầy đủ thông tin!");
            response.sendRedirect(request.getContextPath() + "/careers");
            return;
        }

        try {
            // ========== 1. TẠO THÔNG BÁO CHO USER ==========
            logger.info("Creating user notification...");
            Notification userNotification = NotificationTemplateUtil.createShipperApplicationUserNotification(
                    user.getUserId(),
                    firstName,
                    lastName,
                    vehicleType
            );

            boolean userNotifSuccess = notificationService.createNotification(userNotification);
            if (userNotifSuccess) {
                logger.info("✓ User notification created");
            } else {
                logger.warn("✗ Failed to create user notification");
            }

            // ========== 2. TẠO THÔNG BÁO CHO TẤT CẢ ADMIN ==========
            logger.info("Creating admin notifications...");
            List<Admin> admins = userService.findAllAdmins();
            String userFullName = firstName + " " + lastName;

            int adminNotifCount = 0;
            for (Admin admin : admins) {
                Notification adminNotification = NotificationTemplateUtil.createShipperApplicationAdminNotification(
                        admin.getUserId(),
                        userFullName,
                        email,
                        phone,                    // ← THÊM
                        gender,                   // ← THÊM
                        licenseNumber,            // ← THÊM
                        vehicleType,
                        vehiclePlate,             // ← THÊM
                        address,                  // ← THÊM (địa chỉ hiện tại)
                        experience                // ← THÊM (kinh nghiệm)
                );

                boolean adminNotifSuccess = notificationService.createNotification(adminNotification);
                if (adminNotifSuccess) {
                    adminNotifCount++;
                }
            }
            logger.info("✓ Created {} admin notifications (total admins: {})", adminNotifCount, admins.size());

            // ========== 3. GỬI EMAIL XÁC NHẬN ==========
            logger.info("Sending confirmation email...");
            boolean emailSuccess = EmailUtilBrevo.sendShipperApplicationConfirmation(
                    email,
                    userFullName,
                    vehicleType
            );

            if (emailSuccess) {
                logger.info("✓ Confirmation email sent to: {}", email);
            } else {
                logger.warn("✗ Failed to send confirmation email");
            }

            // ========== 4. THÔNG BÁO THÀNH CÔNG ==========
            SessionUtil.setSuccessMessage(request,
                    "Đơn đăng ký Tài xế Giao hàng đã được gửi thành công! Vui lòng kiểm tra email để biết thêm chi tiết.");
            response.sendRedirect(request.getContextPath() + "/");

        } catch (Exception e) {
            logger.error("✗ Error processing shipper application", e);
            SessionUtil.setErrorMessage(request, "Có lỗi xảy ra khi gửi đơn. Vui lòng thử lại sau!");
            response.sendRedirect(request.getContextPath() + "/careers");
        }
    }
}